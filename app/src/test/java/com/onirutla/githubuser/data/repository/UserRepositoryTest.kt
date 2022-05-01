package com.onirutla.githubuser.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import com.onirutla.githubuser.DummyData
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.db.GithubUserDatabase
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.Response
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    // Dependency
    @Mock
    private lateinit var localDataSource: LocalDataSource
    @Mock
    private lateinit var remoteDataSource: RemoteDataSource
    @Mock
    private lateinit var githubUserDatabase: GithubUserDatabase
    @Mock
    private lateinit var testDispatcher: CoroutineDispatcher

    // Class Under Test
    private lateinit var userRepository: UserRepository

    // Parameter Function
    private val username = "a"

    // Arrange Value
    private val fromDbSuccessUserEntity = flowOf(DummyData.userEntity)
    private val fromDbEmptyUserEntities = flowOf<List<UserEntity>>(emptyList())
    private val fromDbEmptyUserEntity = flowOf(UserEntity())

    private val favoritesFlow = flowOf(DummyData.favorites)

    private val fromNetworkSuccessResponse = Response.Success(DummyData.userResponse)
    private val fromNetworkErrorResponse = Response.Error<UserResponse>()

    @Before
    fun setUp() {
        testDispatcher = TestCoroutineDispatcher()
        userRepository = UserRepositoryImpl(
            remoteDataSource,
            localDataSource,
            githubUserDatabase,
            testDispatcher
        )
    }

    @Test
    fun `user repository shouldn't be null`() {
        assertNotNull(userRepository)
        assertNotNull(localDataSource)
        assertNotNull(remoteDataSource)
    }

    @Test
    fun `getDetailBy first item should resource loading`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbSuccessUserEntity)

        // Act
        val actual = userRepository.getDetailBy(username).first()

        // Assert
        assertNotNull(actual)
        assertEquals(Resource.Loading<UserEntity>(), actual)
    }

    @Test
    fun `getDetailBy should return loading and success when data is found in database`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbSuccessUserEntity)

            // Act
            val actual = userRepository.getDetailBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Success(DummyData.userEntity), actual.last())

            // Verify
            verify(localDataSource).getDetailBy(username)
        }

    @Test
    fun `getDetailBy should return loading and success when data is not found in database but found in network`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkSuccessResponse)

            // Act
            val actual = userRepository.getDetailBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Success(DummyData.userEntity), actual.last())

            // Verify
            verify(localDataSource).getDetailBy(username)
            verify(remoteDataSource).getDetailBy(username)
        }

    @Test
    fun `getDetailBy should return loading and error when data is not found in database and network`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkErrorResponse)

            // Act
            val actual = userRepository.getDetailBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Error<UserEntity>(null), actual.last())

            // Verify
            verify(localDataSource).getDetailBy(username)
            verify(remoteDataSource).getDetailBy(username)
        }

    @Test
    fun `getFavorite should return success when data is found in database`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getFavorite()).thenReturn(favoritesFlow)

            // Act
            val actual = userRepository.getFavorite().toList()

            // Assert
            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Success(DummyData.favorites), actual.last())

            // Verify
            verify(localDataSource).getFavorite()
        }

    @Test
    fun `getFavorite should return empty when data is not found in database`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getFavorite()).thenReturn(fromDbEmptyUserEntities)

            // Act
            val actual = userRepository.getFavorite().toList()

            // Assert
            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(
                Resource.Error<List<UserEntity>>("There is no favorite user"),
                actual.last()
            )

            // Verify
            verify(localDataSource).getFavorite()
        }

    @Test
    fun `setUserFavorite should change userEntity isFavorite true to userEntity isFavorite false`() =
        runBlockingTest {
            // Arrange
            val favorite = DummyData.favorite
            `when`(localDataSource.unFavorite(favorite)).thenReturn(DummyData.unFavorite)

            // Act
            val actual = userRepository.setFavorite(favorite)

            // Assert
            assertNotNull(actual)
            assertFalse(actual.isFavorite)

            // Verify
            verify(localDataSource).unFavorite(favorite)
            verify(localDataSource, times(0)).favorite(favorite)
        }

    @Test
    fun `setUserFavorite should change userEntity isFavorite false to userEntity isFavorite true`() =
        runBlockingTest {
            // Arrange
            val unFavorite = DummyData.unFavorite
            `when`(localDataSource.favorite(unFavorite)).thenReturn(DummyData.favorite)

            // Act
            val actual = userRepository.setFavorite(unFavorite)

            // Assert
            assertNotNull(actual)
            assertTrue(actual.isFavorite)

            // Verify
            verify(localDataSource).favorite(unFavorite)
            verify(localDataSource, times(0)).unFavorite(unFavorite)
        }
}

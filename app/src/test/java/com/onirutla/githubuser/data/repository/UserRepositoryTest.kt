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
import kotlinx.coroutines.flow.flow
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
    private val fromDbSuccessUserEntities = flow { emit(DummyData.userEntities) }
    private val fromDbSuccessUserEntity = flow { emit(DummyData.userEntity) }
    private val fromDbEmptyUserEntities = flow<List<UserEntity>> { emit(emptyList()) }
    private val fromDbEmptyUserEntity = flow { emit(UserEntity()) }

    private val favorites = flow { emit(DummyData.favorites) }

    private val fromNetworkSuccessResponses = Response.Success(DummyData.userResponses)
    private val fromNetworkSuccessResponse = Response.Success(DummyData.userResponse)
    private val fromNetworkErrorResponses = Response.Error<List<UserResponse>>()
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
    fun `getUserDetail first item should resource loading`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbSuccessUserEntity)

        // Act
        val actual = userRepository.getDetailBy(username).first()

        // Assert
        assertNotNull(actual)
        assertEquals(Resource.Loading<UserEntity>(), actual)
    }

    @Test
    fun `getUserDetail should return loading and success when data is found in database`() =
        runBlockingTest {
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbSuccessUserEntity)

            val actual = userRepository.getDetailBy(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Success(DummyData.dto), actual.last())

            verify(localDataSource).getDetailBy(username)
        }

    @Test
    fun `getUserDetail should return loading and success when data is not found in database but found in network`() =
        runBlockingTest {
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkSuccessResponse)

            val actual = userRepository.getDetailBy(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Success(DummyData.dto), actual.last())

            verify(localDataSource).getDetailBy(username)
            verify(remoteDataSource).getDetailBy(username)
        }

    @Test
    fun `getUserDetail should return loading and error when data is not found in database and network`() =
        runBlockingTest {
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkErrorResponse)

            val actual = userRepository.getDetailBy(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Error<UserEntity>(null), actual.last())

            verify(localDataSource).getDetailBy(username)
            verify(remoteDataSource).getDetailBy(username)

        }

    @Test
    fun `getUsersFavorite should return success when data is found in database`() =
        runBlockingTest {
            `when`(localDataSource.getFavorite()).thenReturn(favorites)

            val actual = userRepository.getFavorite().toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Success(DummyData.favoriteDtos), actual.last())

            verify(localDataSource).getFavorite()
        }

    @Test
    fun `getUsersFavorite should return empty when data is not found in database`() =
        runBlockingTest {
            `when`(localDataSource.getFavorite()).thenReturn(fromDbEmptyUserEntities)

            val actual = userRepository.getFavorite().toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(
                Resource.Error<List<UserEntity>>("There is no favorite user"),
                actual.last()
            )

            verify(localDataSource).getFavorite()
        }

    @Test
    fun `setUserFavorite should change userEntity isFavorite true to userEntity isFavorite false`() =
        runBlockingTest {
            val favorite = DummyData.favorite
            `when`(localDataSource.unFavorite(favorite)).thenReturn(DummyData.unFavorite)

            val actual = userRepository.setUserFavorite(favorite)

            assertNotNull(actual)
            assertFalse(actual.isFavorite)

            verify(localDataSource).unFavorite(favorite)
            verify(localDataSource, times(0)).favorite(favorite)
        }

    @Test
    fun `setUserFavorite should change userEntity isFavorite false to userEntity isFavorite true`() =
        runBlockingTest {
            val unFavorite = DummyData.unFavorite
            `when`(localDataSource.favorite(unFavorite)).thenReturn(DummyData.favorite)

            val actual = userRepository.setUserFavorite(unFavorite)
            assertNotNull(actual)
            assertTrue(actual.isFavorite)

            verify(localDataSource).favorite(unFavorite)
            verify(localDataSource, times(0)).unFavorite(unFavorite)
        }
}

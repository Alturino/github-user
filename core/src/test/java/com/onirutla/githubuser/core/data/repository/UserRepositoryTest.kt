package com.onirutla.githubuser.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import com.onirutla.githubuser.core.DummyData
import com.onirutla.githubuser.core.data.UIState
import com.onirutla.githubuser.core.data.source.local.db.GithubUserDatabase
import com.onirutla.githubuser.core.data.source.local.entity.UserEntity
import com.onirutla.githubuser.core.data.source.remote.NetworkResponse
import com.onirutla.githubuser.core.data.source.remote.response.UserResponse
import com.onirutla.githubuser.core.domain.data.User
import com.onirutla.githubuser.core.domain.repository.UserRepository
import com.onirutla.githubuser.core.domain.source.local.LocalDataSource
import com.onirutla.githubuser.core.domain.source.remote.RemoteDataSource
import com.onirutla.githubuser.core.util.entitiesToDomains
import com.onirutla.githubuser.core.util.toUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
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

    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var testScheduler: TestCoroutineScheduler
    private lateinit var testScope: TestScope

    // Class Under Test
    private lateinit var userRepository: UserRepository

    // Parameter Function
    private val username = "a"

    // Arrange Value
    private val fromDbSuccessUserEntity = flowOf(DummyData.userEntity)
    private val fromDbEmptyUserEntities = flowOf<List<UserEntity>>(emptyList())
    private val fromDbEmptyUserEntity = flowOf(UserEntity())

    private val favoritesFlow = flowOf(DummyData.favorites)

    private val fromNetworkSuccessResponse = NetworkResponse.Success(DummyData.userResponse)
    private val fromNetworkErrorResponse = NetworkResponse.Error<UserResponse>()

    @Before
    fun setUp() {
        testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)
        testScope = TestScope(testDispatcher)
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
    fun `getDetailBy first item should resource loading`() = testScope.runTest {
        // Arrange
        `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbSuccessUserEntity)

        // Act
        val actual = userRepository.getDetailBy(username).first()

        // Assert
        assertNotNull(actual)
        assertEquals(UIState.Loading<UserEntity>(), actual)
    }

    @Test
    fun `getDetailBy should return loading and success when data is found in database`() =
        testScope.runTest {
            // Arrange
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbSuccessUserEntity)

            // Act
            val actual = userRepository.getDetailBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertEquals(UIState.Loading<User>(), actual.first())
            assertEquals(UIState.Success(DummyData.userEntity.toUser()), actual.last())

            // Verify
            verify(localDataSource).getDetailBy(username)
        }

    @Test
    fun `getDetailBy should return loading and success when data is not found in database but found in network`() =
        testScope.runTest {
            // Arrange
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkSuccessResponse)

            // Act
            val actual = userRepository.getDetailBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertEquals(UIState.Loading<UserEntity>(), actual.first())
            assertEquals(UIState.Success(DummyData.userEntity.toUser()), actual.last())

            // Verify
            verify(localDataSource).getDetailBy(username)
            verify(remoteDataSource).getDetailBy(username)
        }

    @Test
    fun `getDetailBy should return loading and error when data is not found in database and network`() =
        testScope.runTest {
            // Arrange
            `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkErrorResponse)

            // Act
            val actual = userRepository.getDetailBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertEquals(UIState.Loading<UserEntity>(), actual.first())
            assertEquals(UIState.Error<UserEntity>(null), actual.last())

            // Verify
            verify(localDataSource).getDetailBy(username)
            verify(remoteDataSource).getDetailBy(username)
        }

    @Test
    fun `getFavorite should return success when data is found in database`() =
        testScope.runTest {
            // Arrange
            `when`(localDataSource.getFavorite()).thenReturn(favoritesFlow)

            // Act
            val actual = userRepository.getFavorite().toList()

            // Assert
            assertNotNull(actual)
            assertEquals(UIState.Loading<List<User>>(), actual.first())
            assertEquals(UIState.Success(DummyData.favorites.entitiesToDomains()), actual.last())

            // Verify
            verify(localDataSource).getFavorite()
        }

    @Test
    fun `getFavorite should return empty when data is not found in database`() =
        testScope.runTest {
            // Arrange
            `when`(localDataSource.getFavorite()).thenReturn(fromDbEmptyUserEntities)

            // Act
            val actual = userRepository.getFavorite().toList()

            // Assert
            assertNotNull(actual)
            assertEquals(UIState.Loading<List<UserEntity>>(), actual.first())
            assertEquals(
                UIState.Error<List<UserEntity>>("There is no favorite user"),
                actual.last()
            )

            // Verify
            verify(localDataSource).getFavorite()
        }

    @Test
    fun `setFavorite should change userEntity isFavorite true to userEntity isFavorite false`() =
        testScope.runTest {
            // Arrange
            val favorite = DummyData.favorite
            `when`(localDataSource.unFavorite(favorite)).thenReturn(DummyData.unFavorite)

            // Act
            val actual = userRepository.setFavorite(favorite.toUser())

            // Assert
            assertNotNull(actual)
            assertFalse(actual.isFavorite)

            // Verify
            verify(localDataSource).unFavorite(favorite)
            verify(localDataSource, times(0)).favorite(favorite)
        }

    @Test
    fun `setFavorite should change userEntity isFavorite false to userEntity isFavorite true`() =
        runTest {
            // Arrange
            val unFavorite = DummyData.unFavorite
            `when`(localDataSource.favorite(unFavorite)).thenReturn(DummyData.favorite)

            // Act
            val actual = userRepository.setFavorite(unFavorite.toUser())

            // Assert
            assertNotNull(actual)
            assertTrue(actual.isFavorite)

            // Verify
            verify(localDataSource).favorite(unFavorite)
            verify(localDataSource, times(0)).unFavorite(unFavorite)
        }
}

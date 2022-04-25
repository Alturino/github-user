package com.onirutla.githubuser.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.LocalDataSourceImpl
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.NetworkState
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.RemoteDataSourceImpl
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    // Dependency
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var testDispatcher: CoroutineDispatcher

    // Class Under Test
    private lateinit var userRepository: UserRepository

    // Parameter Function
    private val username = "a"

    // Arrange Value
    private val fromDbSuccessUserEntities = flow { emit(DummyData.userEntities) }
    private val fromDbSuccessUserEntity = flow { emit(DummyData.userEntity) }
    private val fromDbEmptyUserEntities = flow<List<UserEntity>> { emit(emptyList()) }
    private val fromDbEmptyUserEntity = flow<UserEntity> { emit(notNull()) }

    private val favorites = flow { emit(DummyData.favorites) }

    private val fromNetworkSuccessResponses = NetworkState.Success(DummyData.userResponses)
    private val fromNetworkSuccessResponse = NetworkState.Success(DummyData.userResponse)
    private val fromNetworkErrorResponses = NetworkState.Error<List<UserResponse>>()
    private val fromNetworkErrorResponse = NetworkState.Error<UserResponse>()

    @Before
    fun setUp() {
        localDataSource = mock(LocalDataSourceImpl::class.java)
        remoteDataSource = mock(RemoteDataSourceImpl::class.java)
        testDispatcher = TestCoroutineDispatcher()
        userRepository = UserRepository(remoteDataSource, localDataSource, testDispatcher)
    }

    @Test
    fun `user repository shouldn't be null`() {
        assertNotNull(userRepository)
        assertNotNull(localDataSource)
        assertNotNull(remoteDataSource)
    }

    @Test
    fun `getUsersSearch first item should resource loading`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getUserSearch(username)).thenReturn(fromDbSuccessUserEntities)

        // Act
        val actual = userRepository.getUsersSearch(username).toList()

        // Assert
        assertNotNull(actual)
        assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())

        verify(localDataSource).getUserSearch(username)
    }

    @Test
    fun `getUserSearch should return success when data is found in database`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getUserSearch(username)).thenReturn(fromDbSuccessUserEntities)
        `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetworkSuccessResponses)

        // Act
        val actual = userRepository.getUsersSearch(username).toList()

        // Assert
        assertNotNull(actual)
        assertTrue(actual.size == 2)
        assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
        assertEquals(Resource.Success(DummyData.userDtos), actual.last())

        // Verify
        verify(localDataSource).getUserSearch(username)
        verify(remoteDataSource, times(0)).getUserSearch(username)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun `getUserSearch should success when data is not found in database but found in network`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getUserSearch(username)).thenReturn(fromDbEmptyUserEntities)
            `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetworkSuccessResponses)

            // Act
            val actual = userRepository.getUsersSearch(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Success(DummyData.userDtos), actual.last())

            // Verify
            verify(localDataSource).getUserSearch(username)
            verify(remoteDataSource).getUserSearch(username)
        }

    @Test
    fun `getUserSearch should return loading and success when data is not found in database but found in network then cache it`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getUserSearch(username)).thenReturn(fromDbEmptyUserEntities)
            `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetworkSuccessResponses)

            // Act
            val actual = userRepository.getUsersSearch(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Success(DummyData.userDtos), actual.last())

            // Verify
            verify(localDataSource).getUserSearch(username)
            verify(localDataSource).insertUsers(DummyData.userEntities)
            verify(remoteDataSource).getUserSearch(username)
        }

    @Test
    fun `getUserSearch should return error when data is not found in database and network`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.getUserSearch(username)).thenReturn(fromDbEmptyUserEntities)
            `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetworkErrorResponses)

            // Act
            val actual = userRepository.getUsersSearch(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Error<List<UserEntity>>(), actual.last())

            // Verify
            verify(localDataSource).getUserSearch(username)
            verify(remoteDataSource).getUserSearch(username)
        }

    @Test
    fun `getUserDetail first item should resource loading`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getUserDetail(username)).thenReturn(fromDbSuccessUserEntity)
        `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetworkSuccessResponse)

        // Act
        val actual = userRepository.getUserDetail(username).first()

        // Assert
        assertNotNull(actual)
        assertEquals(Resource.Loading<UserEntity>(), actual)
    }

    @Test
    fun `getUserDetail should return loading and success when data is found in database`() =
        runBlockingTest {
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDbSuccessUserEntity)
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetworkSuccessResponse)

            val actual = userRepository.getUserDetail(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Success(DummyData.dto), actual.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource, times(0)).getUserDetail(username)
        }

    @Test
    fun `getUserDetail should return loading and success when data is not found in database but found in network`() =
        runBlockingTest {
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetworkSuccessResponse)

            val actual = userRepository.getUserDetail(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Success(DummyData.dto), actual.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource).getUserDetail(username)
        }

    @Test
    fun `getUserDetail should return loading and error when data is not found in database and network`() =
        runBlockingTest {
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetworkErrorResponse)

            val actual = userRepository.getUserDetail(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Error<UserEntity>(null), actual.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource).getUserDetail(username)

        }

    @Test
    fun `getUserFollower should return success when data is found in network`() = runBlockingTest {
        `when`(remoteDataSource.getUserFollower(username)).thenReturn(fromNetworkSuccessResponses)

        val actual = userRepository.getUsersFollower(username).toList()

        assertNotNull(actual)
        assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
        assertEquals(Resource.Success(DummyData.userDtos), actual.last())

        verify(remoteDataSource).getUserFollower(username)
    }

    @Test
    fun `getUserFollower should return error when data is not found in network`() =
        runBlockingTest {
            `when`(remoteDataSource.getUserFollower(username)).thenReturn(fromNetworkErrorResponses)

            val actual = userRepository.getUsersFollower(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Error<List<UserEntity>>(null), actual.last())

            verify(remoteDataSource).getUserFollower(username)
        }

    @Test
    fun `getUserFollowing should return success when data is found in network`() = runBlockingTest {
        `when`(remoteDataSource.getUserFollowing(username)).thenReturn(fromNetworkSuccessResponses)

        val actual = userRepository.getUsersFollowing(username).toList()

        assertNotNull(actual)
        assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
        assertEquals(Resource.Success(DummyData.userDtos), actual.last())

        verify(remoteDataSource).getUserFollowing(username)
    }

    @Test
    fun `getUserFollowing should return error when data is not found in network`() =
        runBlockingTest {
            `when`(remoteDataSource.getUserFollowing(username)).thenReturn(fromNetworkErrorResponses)

            val actual = userRepository.getUsersFollowing(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Error<List<UserEntity>>(null), actual.last())

            verify(remoteDataSource).getUserFollowing(username)
        }

    @Test
    fun `getUsersFavorite should return success when data is found in database`() =
        runBlockingTest {
            `when`(localDataSource.getFavorite()).thenReturn(favorites)

            val actual = userRepository.getUsersFavorite().toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Success(DummyData.favoriteDtos), actual.last())

            verify(localDataSource).getFavorite()
        }

    @Test
    fun `getUsersFavorite should return empty when data is not found in database`() =
        runBlockingTest {
            `when`(localDataSource.getFavorite()).thenReturn(fromDbEmptyUserEntities)

            val actual = userRepository.getUsersFavorite().toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Error<List<UserEntity>>(), actual.last())

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
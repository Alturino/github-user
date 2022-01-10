package com.onirutla.githubuser.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.local.FromDb
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.LocalDataSourceImpl
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.local.entity.toDto
import com.onirutla.githubuser.data.source.remote.FromNetwork
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
class UserRepositoryImplTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    // Dependency
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var testDispatcher: CoroutineDispatcher

    // Class Under Test
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    // Parameter Function
    private val username = "a"

    // Arrange Value
    private val fromDbSuccessUserEntities = flow { emit(FromDb.Success(DummyData.userEntities)) }
    private val fromDbSuccessUserEntity = flow { emit(FromDb.Success(DummyData.userEntity)) }
    private val fromDbEmptyUserEntities = flow<FromDb<List<UserEntity>>> { emit(FromDb.Empty()) }
    private val fromDbEmptyUserEntity = flow<FromDb<UserEntity>> { emit(FromDb.Empty()) }

    private val favorites = flow { emit(FromDb.Success(DummyData.favorites)) }

    private val fromNetworkSuccessResponses = FromNetwork.Success(DummyData.userResponses)
    private val fromNetworkSuccessResponse = FromNetwork.Success(DummyData.userResponse)
    private val fromNetworkErrorResponses = FromNetwork.Error<List<UserResponse>>()
    private val fromNetworkErrorResponse = FromNetwork.Error<UserResponse>()

    @Before
    fun setUp() {
        localDataSource = mock(LocalDataSourceImpl::class.java)
        remoteDataSource = mock(RemoteDataSourceImpl::class.java)
        testDispatcher = TestCoroutineDispatcher()
        userRepositoryImpl = UserRepositoryImpl(remoteDataSource, localDataSource, testDispatcher)
    }

    @Test
    fun `user repository shouldn't be null`() {
        assertNotNull(userRepositoryImpl)
        assertNotNull(localDataSource)
        assertNotNull(remoteDataSource)
    }

    @Test
    fun `getUsersSearch first item should resource loading`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getUserSearch(username)).thenReturn(fromDbSuccessUserEntities)

        // Act
        val actual = userRepositoryImpl.getUsersSearch(username).toList()

        // Assert
        assertNotNull(actual)
        assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())

        verify(localDataSource).getUserSearch(username)
    }

    @Test
    fun `getUserSearch should return success when data is found in database`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getUserSearch(username)).thenReturn(fromDbSuccessUserEntities)
        `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetworkSuccessResponses)

        // Act
        val actual = userRepositoryImpl.getUsersSearch(username).toList()

        // Assert
        assertNotNull(actual)
        assertTrue(actual.size == 2)
        assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
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
            val actual = userRepositoryImpl.getUsersSearch(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
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
            val actual = userRepositoryImpl.getUsersSearch(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
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
            val actual = userRepositoryImpl.getUsersSearch(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
            assertEquals(Resource.Error<List<UserDTO>>(), actual.last())

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
        val actual = userRepositoryImpl.getUserDetail(username).first()

        // Assert
        assertNotNull(actual)
        assertEquals(Resource.Loading<UserDTO>(), actual)
    }

    @Test
    fun `getUserDetail should return loading and success when data is found in database`() =
        runBlockingTest {
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDbSuccessUserEntity)
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetworkSuccessResponse)

            val actual = userRepositoryImpl.getUserDetail(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserDTO>(), actual.first())
            assertEquals(Resource.Success(DummyData.dto), actual.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource, times(0)).getUserDetail(username)
        }

    @Test
    fun `getUserDetail should return loading and success when data is not found in database but found in network`() =
        runBlockingTest {
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDbEmptyUserEntity)
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetworkSuccessResponse)

            val actual = userRepositoryImpl.getUserDetail(username).toList()

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

            val actual = userRepositoryImpl.getUserDetail(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Error<UserDTO>(null), actual.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource).getUserDetail(username)

        }

    @Test
    fun `getUserFollower should return success when data is found in network`() = runBlockingTest {
        `when`(remoteDataSource.getUserFollower(username)).thenReturn(fromNetworkSuccessResponses)

        val actual = userRepositoryImpl.getUsersFollower(username).toList()

        assertNotNull(actual)
        assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
        assertEquals(Resource.Success(DummyData.userDtos), actual.last())

        verify(remoteDataSource).getUserFollower(username)
    }

    @Test
    fun `getUserFollower should return error when data is not found in network`() =
        runBlockingTest {
            `when`(remoteDataSource.getUserFollower(username)).thenReturn(fromNetworkErrorResponses)

            val actual = userRepositoryImpl.getUsersFollower(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
            assertEquals(Resource.Error<List<UserDTO>>(null), actual.last())

            verify(remoteDataSource).getUserFollower(username)
        }

    @Test
    fun `getUserFollowing should return success when data is found in network`() = runBlockingTest {
        `when`(remoteDataSource.getUserFollowing(username)).thenReturn(fromNetworkSuccessResponses)

        val actual = userRepositoryImpl.getUsersFollowing(username).toList()

        assertNotNull(actual)
        assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
        assertEquals(Resource.Success(DummyData.userDtos), actual.last())

        verify(remoteDataSource).getUserFollowing(username)
    }

    @Test
    fun `getUserFollowing should return error when data is not found in network`() =
        runBlockingTest {
            `when`(remoteDataSource.getUserFollowing(username)).thenReturn(fromNetworkErrorResponses)

            val actual = userRepositoryImpl.getUsersFollowing(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
            assertEquals(Resource.Error<List<UserDTO>>(null), actual.last())

            verify(remoteDataSource).getUserFollowing(username)
        }

    @Test
    fun `getUsersFavorite should return success when data is found in database`() =
        runBlockingTest {
            `when`(localDataSource.getFavorite()).thenReturn(favorites)

            val actual = userRepositoryImpl.getUsersFavorite().toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
            assertEquals(Resource.Success(DummyData.favoriteDtos), actual.last())

            verify(localDataSource).getFavorite()
        }

    @Test
    fun `getUsersFavorite should return empty when data is not found in database`() =
        runBlockingTest {
            `when`(localDataSource.getFavorite()).thenReturn(fromDbEmptyUserEntities)

            val actual = userRepositoryImpl.getUsersFavorite().toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<List<UserDTO>>(), actual.first())
            assertEquals(Resource.Error<List<UserDTO>>(), actual.last())

            verify(localDataSource).getFavorite()
        }

    @Test
    fun `setUserFavorite should change userEntity isFavorite true to userEntity isFavorite false`() =
        runBlockingTest {
            val favorite = DummyData.favorite
            `when`(localDataSource.unFavorite(favorite)).thenReturn(DummyData.unFavorite)

            val actual = userRepositoryImpl.setUserFavorite(favorite.toDto())

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

            val actual = userRepositoryImpl.setUserFavorite(unFavorite.toDto())
            assertNotNull(actual)
            assertTrue(actual.isFavorite)

            verify(localDataSource).favorite(unFavorite)
            verify(localDataSource, times(0)).unFavorite(unFavorite)
        }
}

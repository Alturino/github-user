package com.onirutla.githubuser.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.local.FromDb
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.local.entity.toDto
import com.onirutla.githubuser.data.source.remote.NetworkState
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.response.SearchResponse
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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

    private lateinit var username: String

    // Class Under Test
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        localDataSource = mock(LocalDataSource::class.java)
        remoteDataSource = mock(RemoteDataSource::class.java)
        userRepository = UserRepository(remoteDataSource, localDataSource)
        username = "a"
    }

    @Test
    fun `user repository shouldn't be null`() {
        assertNotNull(userRepository)
        assertNotNull(localDataSource)
        assertNotNull(remoteDataSource)
    }

    @Test
    fun `getUsersSearch first item should resource loading`(): Unit = runBlocking {
        // Arrange
        val fromDb = flow { emit(FromDb.Success(DummyData.userEntities)) }
        `when`(localDataSource.getUserSearch(username)).thenReturn(fromDb)

        val fromNetwork = NetworkState.Success(DummyData.searchResponse)
        `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetwork)

        // Act
        val underTest = userRepository.getUsersSearch(username).first()

        // Assert
        assertNotNull(underTest)
        assertEquals(Resource.Loading<List<UserDTO>>(), underTest)
    }

    @Test
    fun `getUserSearch should return loading and success when data is found in database`(): Unit =
        runBlocking {
            // Arrange
            val fromDb = flow { emit(FromDb.Success(DummyData.userEntities)) }
            `when`(localDataSource.getUserSearch(username)).thenReturn(fromDb)

            val fromNetwork = NetworkState.Success(DummyData.searchResponse)
            `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetwork)

            // Act
            val underTest = userRepository.getUsersSearch(username).toList()

            // Assert
            assertNotNull(underTest)
            assertTrue(underTest.size == 2)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Success(DummyData.userDtos), underTest.last())

            // Verify
            verify(localDataSource).getUserSearch(username)
            verify(remoteDataSource, times(0)).getUserSearch(username)
        }

    @Test
    fun `getUserSearch should return loading and success when data is not found in database but found in network`(): Unit =
        runBlocking {
            // Arrange
            val fromDb = flow { emit(FromDb.Empty<List<UserEntity>>()) }
            `when`(localDataSource.getUserSearch(username)).thenReturn(fromDb)

            val fromNetwork = NetworkState.Success(DummyData.searchResponse)
            `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetwork)

            // Act
            val underTest = userRepository.getUsersSearch(username).toList()

            // Assert
            assertNotNull(underTest)
            assertTrue(underTest.size == 2)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Success(DummyData.userDtos), underTest.last())

            // Verify
            verify(localDataSource).getUserSearch(username)
            verify(remoteDataSource).getUserSearch(username)
        }

    @Test
    fun `getUserSearch should return loading and success when data is not found in database but found in network then cache it`(): Unit =
        runBlocking {
            // Arrange
            val fromDb = flow { emit(FromDb.Empty<List<UserEntity>>()) }
            `when`(localDataSource.getUserSearch(username)).thenReturn(fromDb)

            val fromNetwork = NetworkState.Success(DummyData.searchResponse)
            `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetwork)

            // Act
            val underTest = userRepository.getUsersSearch(username).toList()

            // Assert
            assertNotNull(underTest)
            assertTrue(underTest.size == 2)
            assertEquals(
                "first emitted item must be loading",
                Resource.Loading<List<UserDTO>>(),
                underTest.first()
            )
            assertEquals(Resource.Success(DummyData.userDtos), underTest.last())

            // Verify
            verify(localDataSource).getUserSearch(username)
            verify(localDataSource).insertUsers(DummyData.userEntities)
            verify(remoteDataSource).getUserSearch(username)
        }

    @Test
    fun `getUserSearch should return loading and error when data is not found in database and network`(): Unit =
        runBlocking {
            // Arrange
            val fromDb = flow { emit(FromDb.Empty<List<UserEntity>>()) }
            `when`(localDataSource.getUserSearch(username)).thenReturn(fromDb)

            val fromNetwork = NetworkState.Error<SearchResponse>()
            `when`(remoteDataSource.getUserSearch(username)).thenReturn(fromNetwork)

            // Act
            val underTest = userRepository.getUsersSearch(username).toList()

            // Assert
            assertNotNull(underTest)
            assertTrue(underTest.size == 2)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Error<List<UserDTO>>(null), underTest.last())

            // Verify
            verify(localDataSource).getUserSearch(username)
            verify(remoteDataSource).getUserSearch(username)
        }

    @Test
    fun `getUserDetail first item should resource loading`(): Unit = runBlocking {
        // Arrange
        val fromDb = flow { emit(FromDb.Success(DummyData.userEntity)) }
        `when`(localDataSource.getUserDetail(username)).thenReturn(fromDb)

        val fromNetwork = NetworkState.Success(DummyData.userResponse)
        `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetwork)

        // Act
        val underTest = userRepository.getUserDetail(username).first()

        // Assert
        assertNotNull(underTest)
        assertEquals(Resource.Loading<UserDTO>(), underTest)
    }

    @Test
    fun `getUserDetail should return loading and success when data is found in database`(): Unit =
        runBlocking {
            val fromDb = flow { emit(FromDb.Success(DummyData.userEntity)) }
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDb)

            val fromNetwork = NetworkState.Success(DummyData.userResponse)
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetwork)

            val underTest = userRepository.getUserDetail(username).toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<UserDTO>(), underTest.first())
            assertEquals(Resource.Success(DummyData.dto), underTest.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource, times(0)).getUserDetail(username)
        }

    @Test
    fun `getUserDetail should return loading and success when data is not found in database but found in network`(): Unit =
        runBlocking {
            val fromDb = flow { emit(FromDb.Empty<UserEntity>()) }
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDb)

            val fromNetwork = NetworkState.Success(DummyData.userResponse)
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetwork)

            val underTest = userRepository.getUserDetail(username).toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<UserEntity>(), underTest.first())
            assertEquals(Resource.Success(DummyData.dto), underTest.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource).getUserDetail(username)
        }

    @Test
    fun `getUserDetail should return loading and error when data is not found in database and network`(): Unit =
        runBlocking {
            val fromDb = flow { emit(FromDb.Empty<UserEntity>()) }
            `when`(localDataSource.getUserDetail(username)).thenReturn(fromDb)

            val fromNetwork = NetworkState.Error<UserResponse>()
            `when`(remoteDataSource.getUserDetail(username)).thenReturn(fromNetwork)

            val underTest = userRepository.getUserDetail(username).toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<UserEntity>(), underTest.first())
            assertEquals(Resource.Error<UserDTO>(null), underTest.last())

            verify(localDataSource).getUserDetail(username)
            verify(remoteDataSource).getUserDetail(username)

        }

    @Test
    fun `getUserFollower should return success when data is found in network`(): Unit =
        runBlocking {
            val fromNetwork = NetworkState.Success(DummyData.userResponses)
            `when`(remoteDataSource.getUserFollower(username)).thenReturn(fromNetwork)

            val underTest = userRepository.getUsersFollower(username).toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Success(DummyData.userDtos), underTest.last())

            verify(remoteDataSource).getUserFollower(username)
        }

    @Test
    fun `getUserFollower should return error when data is not found in network`(): Unit =
        runBlocking {
            val fromNetwork = NetworkState.Error<List<UserResponse>>()
            `when`(remoteDataSource.getUserFollower(username)).thenReturn(fromNetwork)

            val underTest = userRepository.getUsersFollower(username).toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Error<List<UserDTO>>(null), underTest.last())

            verify(remoteDataSource).getUserFollower(username)
        }

    @Test
    fun `getUserFollowing should return success when data is found in network`(): Unit =
        runBlocking {
            val fromNetwork = NetworkState.Success(DummyData.userResponses)
            `when`(remoteDataSource.getUserFollowing(username)).thenReturn(fromNetwork)

            val underTest = userRepository.getUsersFollowing(username).toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Success(DummyData.userDtos), underTest.last())

            verify(remoteDataSource).getUserFollowing(username)
        }

    @Test
    fun `getUserFollowing should return error when data is not found in network`(): Unit =
        runBlocking {
            val fromNetwork = NetworkState.Error<List<UserResponse>>()
            `when`(remoteDataSource.getUserFollowing(username)).thenReturn(fromNetwork)

            val underTest = userRepository.getUsersFollowing(username).toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Error<List<UserDTO>>(null), underTest.last())

            verify(remoteDataSource).getUserFollowing(username)
        }

    @Test
    fun `getUsersFavorite should return success when data is found in database`(): Unit =
        runBlocking {
            val fromDb = flow { emit(FromDb.Success(DummyData.favorites)) }
            `when`(localDataSource.getFavorite()).thenReturn(fromDb)

            val underTest = userRepository.getUsersFavorite().toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(Resource.Success(DummyData.favoriteDtos), underTest.last())

            verify(localDataSource).getFavorite()
        }

    @Test
    fun `getUsersFavorite should return empty when data is not found in database`(): Unit =
        runBlocking {
            val fromDb = flow { emit(FromDb.Empty<List<UserEntity>>()) }
            `when`(localDataSource.getFavorite()).thenReturn(fromDb)

            val underTest = userRepository.getUsersFavorite().toList()

            assertNotNull(underTest)
            assertEquals(Resource.Loading<List<UserDTO>>(), underTest.first())
            assertEquals(
                Resource.Error<List<UserDTO>>("You don't have any favorite user yet"),
                underTest.last()
            )

            verify(localDataSource).getFavorite()
        }

    @Test
    fun `setUserFavorite should change userEntity isFavorite true to userEntity isFavorite false`(): Unit =
        runBlocking {
            val favorite = DummyData.favorite
            `when`(localDataSource.unFavorite(favorite)).thenReturn(DummyData.unFavorite)

            val underTest = userRepository.setUserFavorite(favorite.toDto())

            assertNotNull(underTest)
            assertFalse(underTest.isFavorite)

            verify(localDataSource).unFavorite(favorite)
            verify(localDataSource, times(0)).favorite(favorite)
        }

    @Test
    fun `setUserFavorite should change userEntity isFavorite false to userEntity isFavorite true`(): Unit =
        runBlocking {
            val unFavorite = DummyData.unFavorite
            `when`(localDataSource.favorite(unFavorite)).thenReturn(DummyData.favorite)

            val underTest = userRepository.setUserFavorite(unFavorite.toDto())
            assertNotNull(underTest)
            assertTrue(underTest.isFavorite)

            verify(localDataSource).favorite(unFavorite)
            verify(localDataSource, times(0)).unFavorite(unFavorite)
        }
}
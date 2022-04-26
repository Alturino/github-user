package com.onirutla.githubuser.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.Response
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions

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
    private val fromDbEmptyUserEntity = flow { emit(UserEntity()) }

    private val favorites = flow { emit(DummyData.favorites) }

    private val fromNetworkSuccessResponses = Response.Success(DummyData.userResponses)
    private val fromNetworkSuccessResponse = Response.Success(DummyData.userResponse)
    private val fromNetworkErrorResponses = Response.Error<List<UserResponse>>()
    private val fromNetworkErrorResponse = Response.Error<UserResponse>()

    @Before
    fun setUp() {
        localDataSource = mock(LocalDataSource::class.java)
        remoteDataSource = mock(RemoteDataSource::class.java)
        testDispatcher = TestCoroutineDispatcher()
        userRepository = UserRepositoryImpl(remoteDataSource, localDataSource, testDispatcher)
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
        `when`(localDataSource.searchBy(username)).thenReturn(fromDbSuccessUserEntities)

        // Act
        val actual = userRepository.searchBy(username).toList()

        // Assert
        assertNotNull(actual)
        assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())

        verify(localDataSource).searchBy(username)
    }

    @Test
    fun `getUserSearch should return success when data is found in database`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.searchBy(username)).thenReturn(fromDbSuccessUserEntities)
        `when`(remoteDataSource.searchBy(username)).thenReturn(fromNetworkSuccessResponses)

        // Act
        val actual = userRepository.searchBy(username).toList()

        // Assert
        assertNotNull(actual)
        assertTrue(actual.size == 2)
        assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
        assertEquals(Resource.Success(DummyData.userDtos), actual.last())

        // Verify
        verify(localDataSource).searchBy(username)
        verify(remoteDataSource, times(0)).searchBy(username)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun `getUserSearch should success when data is not found in database but found in network`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.searchBy(username)).thenReturn(fromDbEmptyUserEntities)
            `when`(remoteDataSource.searchBy(username)).thenReturn(fromNetworkSuccessResponses)

            // Act
            val actual = userRepository.searchBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Success(DummyData.userDtos), actual.last())

            // Verify
            verify(localDataSource).searchBy(username)
            verify(remoteDataSource).searchBy(username)
        }

    @Test
    fun `getUserSearch should return loading and success when data is not found in database but found in network then cache it`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.searchBy(username)).thenReturn(fromDbEmptyUserEntities)
            `when`(remoteDataSource.searchBy(username)).thenReturn(fromNetworkSuccessResponses)

            // Act
            val actual = userRepository.searchBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Success(DummyData.userDtos), actual.last())

            // Verify
            verify(localDataSource).searchBy(username)
            verify(localDataSource).insertUsers(DummyData.userEntities)
            verify(remoteDataSource).searchBy(username)
        }

    @Test
    fun `getUserSearch should return error when data is not found in database and network`() =
        runBlockingTest {
            // Arrange
            `when`(localDataSource.searchBy(username)).thenReturn(fromDbEmptyUserEntities)
            `when`(remoteDataSource.searchBy(username)).thenReturn(fromNetworkErrorResponses)

            // Act
            val actual = userRepository.searchBy(username).toList()

            // Assert
            assertNotNull(actual)
            assertTrue(actual.size == 2)
            assertEquals(Resource.Loading<List<UserEntity>>(), actual.first())
            assertEquals(Resource.Error<List<UserEntity>>(), actual.last())

            // Verify
            verify(localDataSource).searchBy(username)
            verify(remoteDataSource).searchBy(username)
        }

    @Test
    fun `getUserDetail first item should resource loading`() = runBlockingTest {
        // Arrange
        `when`(localDataSource.getDetailBy(username)).thenReturn(fromDbSuccessUserEntity)
        `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkSuccessResponse)

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
            `when`(remoteDataSource.getDetailBy(username)).thenReturn(fromNetworkSuccessResponse)

            val actual = userRepository.getDetailBy(username).toList()

            assertNotNull(actual)
            assertEquals(Resource.Loading<UserEntity>(), actual.first())
            assertEquals(Resource.Success(DummyData.dto), actual.last())

            verify(localDataSource).getDetailBy(username)
            verify(remoteDataSource, times(0)).getDetailBy(username)
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

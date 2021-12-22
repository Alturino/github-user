package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.repository.DummyData
import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class LocalDataSourceImplTest {

    // Dependency
    private lateinit var userDao: UserDao
    private lateinit var testDispatcher: CoroutineDispatcher

    // Class under test
    private lateinit var localDataSourceImpl: LocalDataSourceImpl

    // Parameter Function
    private val username = "a"

    // Arrange Value
    private val entitiesSuccess = flow { emit(DummyData.userEntities) }
    private val entitiesEmpty = flow<List<UserEntity>> { emit(listOf()) }
    private val entitySuccess = flow { emit(DummyData.userEntity) }
    private val entityEmpty = flow { emit(null) }

    private val entitiesFavoriteSuccess = flow { emit(DummyData.favorites) }

    @Before
    fun setUp() {
        userDao = mock(UserDao::class.java)
        testDispatcher = TestCoroutineDispatcher()
        localDataSourceImpl = LocalDataSourceImpl(userDao, testDispatcher)
    }

    @Test
    fun `instance shouldn't be null`() {
        assertNotNull("user dao shouldn't be null", userDao)
        assertNotNull("local data source shouldn't be null", localDataSourceImpl)
    }

    @Test
    fun `getUserSearch should return success when data is found`() = runBlockingTest {
        `when`(userDao.getUserSearch(username)).thenReturn(entitiesSuccess)

        val actual = localDataSourceImpl.getUserSearch(username).first()

        assertNotNull(actual)
        assertEquals(FromDb.Success(DummyData.userEntities), actual)
        assertEquals(entitiesSuccess.first().size, (actual as FromDb.Success).data.size)

        verify(userDao).getUserSearch(username)
    }

    @Test
    fun `getUserSearch should return empty when data is null or empty`() =
        runBlockingTest {
            `when`(userDao.getUserSearch(username)).thenReturn(entitiesEmpty)

            val actual = localDataSourceImpl.getUserSearch(username).first()

            assertNotNull(actual)
            assertEquals(
                FromDb.Empty<List<UserEntity>>("You don't have any favorite yet"),
                actual
            )

            verify(userDao).getUserSearch(username)
        }

    @Test
    fun `getFavorite should return success when data is found`() = runBlockingTest {
        `when`(userDao.getFavorites()).thenReturn(entitiesFavoriteSuccess)

        val actual = localDataSourceImpl.getFavorite().first()

        assertNotNull(actual)
        assertEquals(FromDb.Success(DummyData.favorites), actual)
        assertEquals(DummyData.favorites, (actual as FromDb.Success).data)

        verify(userDao).getFavorites()
    }

    @Test
    fun `getFavorite should return empty when data is not found`() = runBlockingTest {
        `when`(userDao.getFavorites()).thenReturn(entitiesEmpty)

        val actual = localDataSourceImpl.getFavorite().first()

        assertNotNull(actual)
        assertEquals(FromDb.Empty<List<UserEntity>>("You don't have any favorite yet"), actual)

        verify(userDao).getFavorites()
    }

    @Test
    fun `getUserDetail should return success when data is found`() = runBlockingTest {
        `when`(userDao.getUserDetail(username)).thenReturn(entitySuccess)

        val actual = localDataSourceImpl.getUserDetail(username).first()

        assertNotNull(actual)
        assertEquals(FromDb.Success(DummyData.userEntity), actual)

        verify(userDao).getUserDetail(username)
    }

    @Test
    fun `getUserDetail should return empty when data is not found`() = runBlockingTest {
        `when`(userDao.getUserDetail(username)).thenReturn(entityEmpty)

        val actual = localDataSourceImpl.getUserDetail(username).first()

        assertNotNull(actual)
        assertEquals(FromDb.Empty<UserEntity>("User not found in database"), actual)

        verify(userDao).getUserDetail(username)
    }

    @Test
    fun `insertUsers should insert data to database when list is not null`() = runBlockingTest {
        val userEntities = DummyData.userEntities
        `when`(userDao.insertUsers(listOf())).thenReturn(Unit)

        val actual = localDataSourceImpl.insertUsers(userEntities)

        assertNotNull(actual)
        verify(userDao).insertUsers(userEntities)
    }

    @Test
    fun `insertUserDetail should insert data to database when user is not null`() =
        runBlockingTest {
            val user = DummyData.userEntity
            `when`(userDao.insertUser(user)).thenReturn(Unit)

            val actual = localDataSourceImpl.insertUserDetail(user)

            assertNotNull(actual)
            verify(userDao).insertUser(user)
        }

    @Test
    fun `favorite should change isFavorite to true and update to database`() = runBlockingTest {
        val user = DummyData.userEntity
        val unFavorite = user.copy(isFavorite = true)
        `when`(userDao.updateFavorite(unFavorite)).thenReturn(Unit)

        val actual = localDataSourceImpl.favorite(unFavorite)

        assertNotNull(actual)
        assertTrue(actual.isFavorite)

        verify(userDao).updateFavorite(unFavorite)
    }

    @Test
    fun `unFavorite should change isFavorite to false and update to database`() = runBlockingTest {
        val user = DummyData.favorite
        val favorite = user.copy(isFavorite = false)
        `when`(userDao.updateFavorite(favorite)).thenReturn(Unit)

        val actual = localDataSourceImpl.unFavorite(favorite)

        assertNotNull(actual)
        assertFalse(actual.isFavorite)

        verify(userDao).updateFavorite(favorite)
    }
}

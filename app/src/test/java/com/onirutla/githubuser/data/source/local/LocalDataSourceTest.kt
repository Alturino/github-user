package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.repository.DummyData
import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class LocalDataSourceTest {

    // Dependency
    private lateinit var userDao: UserDao

    // Class under test
    private lateinit var localDataSource: LocalDataSource

    private lateinit var username: String


    @Before
    fun setUp() {
        userDao = mock(UserDao::class.java)
        localDataSource = LocalDataSource(userDao)
        username = "a"
    }

    @Test
    fun `instance shouldn't be null`() {
        assertNotNull("user dao shouldn't be null", userDao)
        assertNotNull("local data source shouldn't be null", localDataSource)
    }

    @Test
    fun `getUserSearch should return success when data is found`() = runBlockingTest {
        val fromDao = flow { emit(DummyData.userEntities) }
        `when`(userDao.getUserSearch(username)).thenReturn(fromDao)

        val underTest = localDataSource.getUserSearch(username).first()

        assertNotNull(underTest)
        assertEquals(underTest, FromDb.Success(DummyData.userEntities))
        assertEquals(
            "size should be the same",
            (underTest as FromDb.Success).data.size,
            fromDao.first().size
        )

        verify(userDao).getUserSearch(username)
    }

    @Test
    fun `getUserSearch should return empty when data is null or empty`() =
        runBlockingTest {
            val fromDao = flow { emit(listOf<UserEntity>()) }
            `when`(userDao.getUserSearch(username)).thenReturn(fromDao)

            val underTest = localDataSource.getUserSearch(username).first()

            assertNotNull(underTest)
            assertEquals(
                "underTest should be FromDb.Empty(You don't have any favorite yet)",
                underTest,
                FromDb.Empty<List<UserEntity>>("You don't have any favorite yet")
            )

            verify(userDao).getUserSearch(username)
        }

    @Test
    fun `getFavorite should return success when data is found`() = runBlockingTest {
        val fromDao = flow { emit(DummyData.favorites) }
        `when`(userDao.getFavorites()).thenReturn(fromDao)

        val underTest = localDataSource.getFavorite().first()

        assertNotNull(underTest)
        assertEquals(underTest, FromDb.Success(DummyData.favorites))

        verify(userDao).getFavorites()
    }

    @Test
    fun `getFavorite should return empty when data is not found`() = runBlockingTest {
        val fromDao = flow { emit(listOf<UserEntity>()) }
        `when`(userDao.getFavorites()).thenReturn(fromDao)

        val underTest = localDataSource.getFavorite().first()

        assertNotNull(underTest)
        assertEquals(underTest, FromDb.Empty<List<UserEntity>>("You don't have any favorite yet"))

        verify(userDao).getFavorites()
    }

    @Test
    fun `getUserDetail should return success when data is found`() = runBlockingTest {
        val fromDao = flow { emit(DummyData.userEntity) }
        `when`(userDao.getUserDetail(username)).thenReturn(fromDao)

        val underTest = localDataSource.getUserDetail(username).first()

        assertNotNull(underTest)
        assertEquals(underTest, FromDb.Success(DummyData.userEntity))

        verify(userDao).getUserDetail(username)
    }

    @Test
    fun `getUserDetail should return empty when data is not found`() = runBlockingTest {
        val fromDao = flow { emit(null) }
        `when`(userDao.getUserDetail(username)).thenReturn(fromDao)

        val underTest = localDataSource.getUserDetail(username).first()

        assertNotNull(underTest)
        assertEquals(underTest, FromDb.Empty<UserEntity>("User not found in database"))

        verify(userDao).getUserDetail(username)
    }

    @Test
    fun `insertUsers should insert data to database when list is not null`() = runBlockingTest {
        val userEntities = DummyData.userEntities
        `when`(userDao.insertUsers(listOf())).thenReturn(Unit)

        val underTest = localDataSource.insertUsers(userEntities)

        assertNotNull(underTest)
        verify(userDao).insertUsers(userEntities)
    }

    @Test
    fun `insertUserDetail should insert data to database when user is not null`() =
        runBlockingTest {
            val user = DummyData.userEntity
            `when`(userDao.insertUser(user)).thenReturn(Unit)

            val underTest = localDataSource.insertUserDetail(user)

            assertNotNull(underTest)
            verify(userDao).insertUser(user)
        }

    @Test
    fun `favorite should change isFavorite to true and update to database`() = runBlockingTest {
        val user = DummyData.userEntity
        val unFavorite = user.copy(isFavorite = true)
        `when`(userDao.updateFavorite(unFavorite)).thenReturn(Unit)

        val underTest = localDataSource.favorite(unFavorite)

        assertNotNull(underTest)
        assertTrue(underTest.isFavorite)

        verify(userDao).updateFavorite(unFavorite)
    }

    @Test
    fun `unFavorite should change isFavorite to false and update to database`() = runBlockingTest {
        val user = DummyData.favorite
        val favorite = user.copy(isFavorite = false)
        `when`(userDao.updateFavorite(favorite)).thenReturn(Unit)

        val underTest = localDataSource.unFavorite(favorite)

        assertNotNull(underTest)
        assertFalse(underTest.isFavorite)

        verify(userDao).updateFavorite(favorite)
    }
}



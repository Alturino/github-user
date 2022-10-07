package com.onirutla.githubuser.core.data.source.local

import com.onirutla.githubuser.core.DummyData
import com.onirutla.githubuser.core.data.source.local.dao.UserDao
import com.onirutla.githubuser.core.data.source.local.entity.UserEntity
import com.onirutla.githubuser.core.domain.source.local.LocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class LocalDataSourceTest {

    // Dependency
    private lateinit var userDao: UserDao

    // Class under test
    private lateinit var localDataSource: LocalDataSource

    // Parameter Function
    private val username = "a"

    // Arrange Value
    private val entitiesSuccess = flowOf(DummyData.userEntities)
    private val entitiesEmpty = flowOf<List<UserEntity>>(emptyList())
    private val entitySuccess = flowOf(DummyData.userEntity)
    private val entityEmpty = flow<UserEntity?> { emit(null) }

    private val entitiesFavoriteSuccess = flow { emit(DummyData.favorites) }

    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var testScheduler: TestCoroutineScheduler
    private lateinit var testScope: TestScope

    @Before
    fun setUp() {
        userDao = mock(UserDao::class.java)
        testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)
        testScope = TestScope(testDispatcher)
        localDataSource = LocalDataSourceImpl(userDao, testDispatcher)
    }

    @Test
    fun `instance shouldn't be null`() {
        assertNotNull("user dao shouldn't be null", userDao)
        assertNotNull("local data source shouldn't be null", localDataSource)
    }

    @Test
    fun `searchBy should return success when data is found`() = testScope.runTest {
        `when`(userDao.getUserSearch(username)).thenReturn(entitiesSuccess)

        val actual = localDataSource.searchBy(username).first()

        assertNotNull(actual)
        assertEquals(DummyData.userEntities, actual)
        assertEquals(entitiesSuccess.first().size, actual.size)

        verify(userDao).getUserSearch(username)
    }

    @Test
    fun `searchBy should return empty when data is null or empty`() =
        testScope.runTest {
            `when`(userDao.getUserSearch(username)).thenReturn(entitiesEmpty)

            val actual = localDataSource.searchBy(username).first()

            assertNotNull(actual)


            verify(userDao).getUserSearch(username)
        }

    @Test
    fun `getFavorite should return success when data is found`() = testScope.runTest {
        `when`(userDao.getFavorites()).thenReturn(entitiesFavoriteSuccess)

        val actual = localDataSource.getFavorite().first()

        assertNotNull(actual)
        assertEquals(DummyData.favorites, actual)
        assertEquals(DummyData.favorites.size, actual.size)

        verify(userDao).getFavorites()
    }

    @Test
    fun `getFavorite should return empty when data is not found`() = testScope.runTest {
        `when`(userDao.getFavorites()).thenReturn(entitiesEmpty)

        val actual = localDataSource.getFavorite().first()

        assertNotNull(actual)

        verify(userDao).getFavorites()
    }

    @Test
    fun `getDetailBy should return success when data is found`() = testScope.runTest {
        `when`(userDao.getUserDetail(username)).thenReturn(entitySuccess)

        val actual = localDataSource.getDetailBy(username).first()

        assertNotNull(actual)
        assertEquals(DummyData.userEntity, actual)

        verify(userDao).getUserDetail(username)
    }

    @Test
    fun `getDetailBy should return empty when data is not found`() = testScope.runTest {
        `when`(userDao.getUserDetail(username)).thenReturn(entityEmpty)

        val actual = localDataSource.getDetailBy(username)

        assertNotNull(actual)
        assertTrue(actual.toList().isEmpty())

        verify(userDao).getUserDetail(username)
    }

    @Test
    fun `insertUsers should insert data to database when list is not null`() = testScope.runTest {
        val userEntities = DummyData.userEntities
        `when`(userDao.insertUsers(*listOf<UserEntity>().toTypedArray())).thenReturn(Unit)

        val actual = localDataSource.insertUsers(*userEntities.toTypedArray())

        assertNotNull(actual)
        verify(userDao).insertUsers(*userEntities.toTypedArray())
    }

    @Test
    fun `insertUserDetail should insert data to database when user is not null`() =
        testScope.runTest {
            val user = DummyData.userEntity
            `when`(userDao.insertUsers(user)).thenReturn(Unit)

            val actual = localDataSource.insertUsers(user)

            assertNotNull(actual)
            verify(userDao).insertUsers(user)
        }

    @Test
    fun `favorite should change isFavorite to true and update to database`() = testScope.runTest {
        val user = DummyData.userEntity
        val unFavorite = user.copy(isFavorite = true)
        `when`(userDao.updateFavorite(unFavorite)).thenReturn(Unit)

        val actual = localDataSource.favorite(unFavorite)

        assertNotNull(actual)
        assertTrue(actual.isFavorite)

        verify(userDao).updateFavorite(unFavorite)
    }

    @Test
    fun `unFavorite should change isFavorite to false and update to database`() =
        testScope.runTest {
            val user = DummyData.favorite
            val favorite = user.copy(isFavorite = false)
            `when`(userDao.updateFavorite(favorite)).thenReturn(Unit)

            val actual = localDataSource.unFavorite(favorite)

            assertNotNull(actual)
            assertFalse(actual.isFavorite)

            verify(userDao).updateFavorite(favorite)
    }
}

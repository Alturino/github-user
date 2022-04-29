package com.onirutla.githubuser.data.source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.onirutla.githubuser.DummyData
import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.db.GithubUserDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class GithubUserDatabaseTest {

    private lateinit var githubUserDatabase: GithubUserDatabase
    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var testScope: TestCoroutineScope

    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        testDispatcher = TestCoroutineDispatcher()
        testScope = TestCoroutineScope(testDispatcher)
        githubUserDatabase =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                GithubUserDatabase::class.java
            ).setTransactionExecutor(testDispatcher.asExecutor())
                .setQueryExecutor(testDispatcher.asExecutor())
                .build()
        userDao = githubUserDatabase.userDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        githubUserDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = testScope.runBlockingTest {
        val expected = DummyData.userEntities
        userDao.insertUsers(expected)
        val actual = userDao.getUserSearch("").first()
        assertEquals(expected, actual)
    }
}
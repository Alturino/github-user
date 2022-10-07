package com.onirutla.githubuser.core

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.onirutla.githubuser.core.data.source.local.dao.UserDao
import com.onirutla.githubuser.core.data.source.local.db.GithubUserDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
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
    private lateinit var testScheduler: TestCoroutineScheduler
    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var testScope: TestScope

    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)
        testScope = TestScope(testDispatcher)
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
    fun writeUserAndReadInList() = testScope.runTest {
        val expected = DummyData.userEntities
        userDao.insertUsers(*expected.toTypedArray())
        val actual = userDao.getUserSearch("").first()
        assertEquals(expected, actual)
    }
}
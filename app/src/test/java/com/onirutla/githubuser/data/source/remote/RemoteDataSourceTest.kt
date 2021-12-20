package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.repository.DummyData
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RemoteDataSourceTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var responses: MutableList<UserResponse>

    private val username: String = "a"

    @Before
    fun setUp() {
        responses = DummyData.userResponses.toMutableList()
        remoteDataSource = FakeRemoteDataSource(responses)
    }

    @Test
    fun `remoteDataSource should not null`() = runBlockingTest {
        assertNotNull(remoteDataSource)
    }

    @Test
    fun `getUserSearch should return success when data is not null and contain users with the same username`() =
        runBlockingTest {
            val expected = NetworkState.Success(responses.filter { it.username.equals(username) })

            val actual = remoteDataSource.getUserSearch(username)

            assertEquals(expected.body, (actual as NetworkState.Success).body)
        }

    @Test
    fun `getUserSearch should return same size when data is not null`() = runBlockingTest {
        val expected = NetworkState.Success(responses.filter { it.username.equals(username) })

        val actual = remoteDataSource.getUserSearch(username)

        assertEquals(expected.body.size, (actual as NetworkState.Success).body.size)
    }

    @Test
    fun `getUserSearch should return error when data is null`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource()
        val expected = NetworkState.Error<List<UserResponse>>()

        val actual = remoteDataSource.getUserSearch(username)

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserSearch should return error when data is empty`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource(emptyList<UserResponse>().toMutableList())

        val expected = NetworkState.Error<List<UserResponse>>()

        val actual = remoteDataSource.getUserSearch(username)

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserDetail should return success when data is not null and contain user with the same username`() =
        runBlockingTest {
            val expected = NetworkState.Success(responses.find { it.username.equals(username) })

            val actual = remoteDataSource.getUserDetail(username)

            assertEquals(expected, actual)
            assertEquals(expected.body?.username, (actual as NetworkState.Success).body.username)
        }

    @Test
    fun `getUserDetail should return error when data is null`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource()

        val expected = NetworkState.Error<UserResponse>()

        val actual = remoteDataSource.getUserDetail(username)

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserDetail should return error when data is empty`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource(emptyList<UserResponse>().toMutableList())

        val expected = NetworkState.Error<UserResponse>()

        val actual = remoteDataSource.getUserDetail(username)

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserFollower should return success when data is not null and contain users with the same username`() =
        runBlockingTest {
            val expected = NetworkState.Success(responses)

            val actual = remoteDataSource.getUserFollower(username)

            assertEquals(expected.body, (actual as NetworkState.Success).body)
        }

    @Test
    fun `getUserFollower should return same size when data is not null`() = runBlockingTest {
        val expected = NetworkState.Success(responses)

        val actual = remoteDataSource.getUserFollower(username)

        assertEquals(expected.body.size, (actual as NetworkState.Success).body.size)
    }

    @Test
    fun `getUserFollower should return error when data is null`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource()
        val expected = NetworkState.Error<List<UserResponse>>()

        val actual = remoteDataSource.getUserFollower(username)

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserFollower should return error when data is empty`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource(emptyList<UserResponse>().toMutableList())

        val expected = NetworkState.Error<List<UserResponse>>()

        val actual = remoteDataSource.getUserFollower(username)

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserFollowing should return success when data is not null and contain users with the same username`() =
        runBlockingTest {
            val expected = NetworkState.Success(responses)

            val actual = remoteDataSource.getUserFollowing(username)

            assertEquals(expected.body, (actual as NetworkState.Success).body)
        }

    @Test
    fun `getUserFollowing should return same size when data is not null`() = runBlockingTest {
        val expected = NetworkState.Success(responses)

        val actual = remoteDataSource.getUserFollowing(username)

        assertEquals(expected.body.size, (actual as NetworkState.Success).body.size)
    }

    @Test
    fun `getUserFollowing should return error when data is null`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource()
        val expected = NetworkState.Error<List<UserResponse>>()

        val actual = remoteDataSource.getUserFollowing(username)

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserFollowing should return error when data is empty`() = runBlockingTest {
        remoteDataSource = FakeRemoteDataSource(emptyList<UserResponse>().toMutableList())

        val expected = NetworkState.Error<List<UserResponse>>()

        val actual = remoteDataSource.getUserFollowing(username)

        assertEquals(expected, actual)
    }
}

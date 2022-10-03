package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.DummyData
import com.onirutla.githubuser.data.source.remote.network.GithubApiService
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {

    @Mock
    private lateinit var githubApiService: GithubApiService

    private lateinit var remoteDataSource: RemoteDataSource

    private val username = "a"
    private val emptyUsername = ""
    private val position = 1
    private val zeroPosition = 0

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSourceImpl(githubApiService)
    }

    @Test
    fun `given username and position searchBy should return NetworkResponse Success`() = runTest {
        `when`(
            githubApiService.searchBy(
                username,
                position
            )
        ).thenReturn(Response.success(DummyData.searchResponse))

        val actual = remoteDataSource.searchBy(username, position)

        assertEquals(NetworkResponse.Success(DummyData.userResponses), actual)

        verify(githubApiService).searchBy(username, position)
    }

    @Test
    fun `given empty username searchBy should return NetworkResponse Error`() = runTest {
        val actual = remoteDataSource.searchBy(emptyUsername, position)

        assertEquals(
            NetworkResponse.Error<List<UserResponse>>(message = "username shouldn't be empty"),
            actual
        )
    }

    @Test
    fun `given position = 0 should searchBy return NetworkResponse Error`() = runTest {
        val actual = remoteDataSource.searchBy(username, zeroPosition)

        assertEquals(
            NetworkResponse.Error<List<UserResponse>>(message = "position shouldn't be zero"),
            actual
        )
    }

    @Test
    fun `given empty username getDetailBy should return NetworkResponse Error`() = runTest {
        val actual = remoteDataSource.getDetailBy(emptyUsername)

        assertEquals(
            NetworkResponse.Error<List<UserResponse>>(message = "username should not be empty"),
            actual
        )
    }

    @Test
    fun `given username getDetailBy return NetworkResponse Success`() = runTest {
        `when`(githubApiService.getDetailBy(username)).thenReturn(Response.success(DummyData.userResponse))

        val actual = remoteDataSource.getDetailBy(username)

        assertEquals(
            NetworkResponse.Success(body = DummyData.userResponse),
            actual
        )

        verify(githubApiService).getDetailBy(username)
    }
}
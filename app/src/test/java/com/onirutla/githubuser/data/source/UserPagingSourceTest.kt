package com.onirutla.githubuser.data.source

import androidx.paging.PagingSource
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import com.onirutla.githubuser.data.source.remote.response.toEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserPagingSourceTest {

    // Dependency
    @Mock
    private lateinit var apiService: (position: Int) -> Response<List<UserResponse>>

    // SUT
    private lateinit var userPagingSource: UserPagingSource

    @Before
    fun setUp() {
        userPagingSource = UserPagingSource(apiService)
    }

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoadOfItemKeyedData() = runBlockingTest {
        val dummyData = mutableListOf(UserResponse())
        repeat(2) {
            dummyData.add(UserResponse())
        }
        `when`(apiService(1)).thenReturn(Response.success(dummyData))
        assertEquals(
            PagingSource.LoadResult.Page(
                data = dummyData.map { it.toEntity() },
                prevKey = null,
                nextKey = 2
            ),
            userPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 11,
                    placeholdersEnabled = false
                )
            )
        )

    }
}
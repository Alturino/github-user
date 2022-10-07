package com.onirutla.githubuser.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import com.onirutla.githubuser.core.DummyData
import com.onirutla.githubuser.core.data.source.remote.response.UserResponse
import com.onirutla.githubuser.core.util.responsesToEntities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    fun loadReturnsPageWhenOnSuccessfulLoadOfItemKeyedData() = runTest {
        `when`(apiService(1)).thenReturn(Response.success(DummyData.userResponses))
        assertEquals(
            Page(
                data = DummyData.userResponses.responsesToEntities(),
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
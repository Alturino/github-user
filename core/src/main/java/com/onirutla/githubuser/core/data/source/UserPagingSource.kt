package com.onirutla.githubuser.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onirutla.githubuser.core.data.source.remote.response.UserResponse
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

internal class UserPagingSource(
    private inline val apiService: suspend (position: Int) -> Response<List<UserResponse>>,
) : PagingSource<Int, UserResponse>() {

    override fun getRefreshKey(state: PagingState<Int, UserResponse>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserResponse> = try {
        val position = params.key ?: 1
        val response = apiService(position).body()
        val nextKey = if (response.isNullOrEmpty()) null else position + 1
        LoadResult.Page(
            data = response.orEmpty(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = nextKey
        )
    } catch (exception: IOException) {
        LoadResult.Error(exception)
    } catch (exception: HttpException) {
        LoadResult.Error(exception)
    }
}

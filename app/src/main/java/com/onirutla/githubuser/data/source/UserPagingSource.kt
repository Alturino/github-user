package com.onirutla.githubuser.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import com.onirutla.githubuser.util.toEntities
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

class UserPagingSource(
    private inline val apiService: suspend (position: Int) -> Response<List<UserResponse>>,
) : PagingSource<Int, UserEntity>() {

    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> = try {
        val position = params.key ?: 1
        val response = apiService(position).body()
        val users = response?.toEntities()
        val nextKey = if (users.isNullOrEmpty()) null else position + 1
        LoadResult.Page(
            data = users.orEmpty(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = nextKey
        )
    } catch (exception: IOException) {
        LoadResult.Error(exception)
    } catch (exception: HttpException) {
        LoadResult.Error(exception)
    }
}

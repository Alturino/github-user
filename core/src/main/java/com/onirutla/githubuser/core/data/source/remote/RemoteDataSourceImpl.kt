package com.onirutla.githubuser.core.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onirutla.githubuser.core.data.source.UserPagingSource
import com.onirutla.githubuser.core.data.source.remote.network.GithubApiService
import com.onirutla.githubuser.core.data.source.remote.response.UserResponse
import com.onirutla.githubuser.core.domain.source.remote.RemoteDataSource
import com.onirutla.githubuser.core.util.Constant.GITHUB_PAGE_SIZE
import com.onirutla.githubuser.core.util.getResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RemoteDataSourceImpl @Inject constructor(
    private val apiService: GithubApiService
) : RemoteDataSource {

    override suspend fun searchBy(
        username: String,
        position: Int
    ): NetworkResponse<List<UserResponse>> {
        if (username.isEmpty())
            return NetworkResponse.Error(message = "username shouldn't be empty")
        if (position == 0)
            return NetworkResponse.Error(message = "position shouldn't be zero")

        return apiService.searchBy(username, position).getResult {
            body()?.items!!
        }
    }

    override suspend fun getDetailBy(username: String): NetworkResponse<UserResponse> {
        if (username.isEmpty() or username.isBlank())
            return NetworkResponse.Error(message = "username should not be empty")
        return apiService.getDetailBy(username).getResult()
    }

    override fun getFollowerPaging(username: String): Flow<PagingData<UserResponse>> =
        Pager(config = PagingConfig(pageSize = GITHUB_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                UserPagingSource { position ->
                    apiService.getFollowerBy(username, position)
                }
            }
        ).flow

    override fun getFollowingPaging(username: String): Flow<PagingData<UserResponse>> =
        Pager(config = PagingConfig(pageSize = GITHUB_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                UserPagingSource { position ->
                    apiService.getFollowingBy(username, position)
                }
            }
        ).flow
}

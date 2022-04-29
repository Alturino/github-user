package com.onirutla.githubuser.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onirutla.githubuser.data.source.UserPagingSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.network.GithubApiService
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import com.onirutla.githubuser.util.Constant.GITHUB_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: GithubApiService
) : RemoteDataSource {

    override suspend fun searchBy(username: String, position: Int): Response<List<UserResponse>> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.searchBy(username, position)
        if (response.isSuccessful)
            Response.Success(body = response.body()?.items!!, message = response.message())
        else
            Response.Error(message = response.message())
    } catch (t: Throwable) {
        Response.Error(message = t.message)
    }

    override suspend fun getDetailBy(username: String): Response<UserResponse> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.getDetailBy(username)
        if (response.isSuccessful)
            Response.Success(body = response.body()!!, message = response.message())
        else
            Response.Error(message = response.message())
    } catch (t: Throwable) {
        Response.Error(message = t.message)
    }

    override suspend fun getFollowerBy(username: String): Response<List<UserResponse>> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.getFollowerBy(username)
        if (response.isSuccessful)
            Response.Success(body = response.body()!!, message = response.message())
        else
            Response.Error(message = response.message())

    } catch (t: Throwable) {
        Response.Error(message = t.message)
    }

    override fun getFollowerPaging(username: String): Flow<PagingData<UserEntity>> =
        Pager(config = PagingConfig(pageSize = GITHUB_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                UserPagingSource { position ->
                    apiService.getFollowerBy(username, position)
                }
            }
        ).flow


    override suspend fun getFollowingBy(username: String): Response<List<UserResponse>> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.getFollowingBy(username)
        if (response.isSuccessful)
            Response.Success(body = response.body()!!, message = response.message())
        else
            Response.Error(message = response.message())
    } catch (t: Throwable) {
        Response.Error(message = t.message)
    }

    override fun getFollowingPaging(username: String): Flow<PagingData<UserEntity>> =
        Pager(config = PagingConfig(pageSize = GITHUB_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                UserPagingSource { position ->
                    apiService.getFollowingBy(username, position)
                }
            }
        ).flow
}

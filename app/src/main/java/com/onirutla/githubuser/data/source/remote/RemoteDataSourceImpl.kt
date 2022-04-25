package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.network.GithubApiService
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: GithubApiService
) : RemoteDataSource {

    override suspend fun searchBy(username: String): NetworkState<List<UserResponse>> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.searchBy(username)
        if (response.isSuccessful)
            NetworkState.Success(body = response.body()?.items!!, message = response.message())
        else
            NetworkState.Error(message = response.message())
    } catch (t: Throwable) {
        NetworkState.Error(message = t.message)
    }

    override suspend fun getDetailBy(username: String): NetworkState<UserResponse> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.getDetailBy(username)
        if (response.isSuccessful)
            NetworkState.Success(body = response.body()!!, message = response.message())
        else
            NetworkState.Error(message = response.message())
    } catch (t: Throwable) {
        NetworkState.Error(message = t.message)
    }

    override suspend fun getFollowerBy(username: String): NetworkState<List<UserResponse>> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.getFollowerBy(username)
        if (response.isSuccessful)
            NetworkState.Success(body = response.body()!!, message = response.message())
        else
            NetworkState.Error(message = response.message())

    } catch (t: Throwable) {
        NetworkState.Error(message = t.message)
    }

    override suspend fun getFollowingBy(username: String): NetworkState<List<UserResponse>> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.getFollowingBy(username)
        if (response.isSuccessful)
            NetworkState.Success(body = response.body()!!, message = response.message())
        else
            NetworkState.Error(message = response.message())
    } catch (t: Throwable) {
        NetworkState.Error(message = t.message)
    }
}

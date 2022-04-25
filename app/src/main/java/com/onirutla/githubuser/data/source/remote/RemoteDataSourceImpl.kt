package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.network.NetworkService
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NetworkService
) : RemoteDataSource {

    override suspend fun searchBy(username: String): NetworkState<List<UserResponse>> = try {
        if (username.isEmpty())
            throw IllegalArgumentException("username shouldn't be empty")
        val response = apiService.getUsersSearch(username)
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
        val response = apiService.getUserDetail(username)
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
        val response = apiService.getUserFollower(username)
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
        val response = apiService.getUserFollowing(username)
        if (response.isSuccessful)
            NetworkState.Success(body = response.body()!!, message = response.message())
        else
            NetworkState.Error(message = response.message())
    } catch (t: Throwable) {
        NetworkState.Error(message = t.message)
    }
}

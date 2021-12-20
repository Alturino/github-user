package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.network.NetworkService
import com.onirutla.githubuser.data.source.remote.response.SearchResponse
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NetworkService
) : RemoteDataSource {

    override suspend fun getUserSearch(username: String): NetworkState<List<UserResponse>> {
        return try {
            val response = apiService.getUsersSearch(username)
            return if (response.isSuccessful)
                NetworkState.Success(body = response.body()?.items!!, message = response.message())
            else
                NetworkState.Error(message = response.message())
        } catch (t: Throwable) {
            NetworkState.Error(message = t.message)
        }
    }

    override suspend fun getUserDetail(username: String): NetworkState<UserResponse> {
        return try {
            val response = apiService.getUserDetail(username)
            return if (response.isSuccessful)
                NetworkState.Success(body = response.body()!!, message = response.message())
            else
                NetworkState.Error(message = response.message())
        } catch (t: Throwable) {
            NetworkState.Error(message = t.message)
        }
    }

    override suspend fun getUserFollower(username: String): NetworkState<List<UserResponse>> {
        return try {
            val response = apiService.getUserFollower(username)
            return if (response.isSuccessful)
                NetworkState.Success(body = response.body()!!, message = response.message())
            else
                NetworkState.Error(message = response.message())

        } catch (t: Throwable) {
            NetworkState.Error(message = t.message)
        }
    }

    override suspend fun getUserFollowing(username: String): NetworkState<List<UserResponse>> {
        return try {
            val response = apiService.getUserFollowing(username)
            return if (response.isSuccessful)
                NetworkState.Success(body = response.body()!!, message = response.message())
            else
                NetworkState.Error(message = response.message())
        } catch (t: Throwable) {
            NetworkState.Error(message = t.message)
        }
    }

}

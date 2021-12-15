package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.network.NetworkService
import com.onirutla.githubuser.data.source.remote.response.SearchResponse
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: NetworkService) {

    suspend fun getUserSearch(username: String): NetworkState<SearchResponse> {
        return try {
            val response = apiService.getUsersSearch(username)
            return if (response.isSuccessful)
                NetworkState.Success(body = response.body()!!, message = response.message())
            else
                NetworkState.Error(message = response.message())
        } catch (t: Throwable) {
            NetworkState.Error(message = t.message)
        }
    }

    suspend fun getUserDetail(username: String): NetworkState<UserResponse> {
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

    suspend fun getUserFollower(username: String): NetworkState<List<UserResponse>> {
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

    suspend fun getUserFollowing(username: String): NetworkState<List<UserResponse>> {
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
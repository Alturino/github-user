package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.network.NetworkService
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NetworkService
) : RemoteDataSource {

    override suspend fun getUserSearch(username: String): FromNetwork<List<UserResponse>> {
        return try {
            val response = apiService.getUsersSearch(username)
            return if (response.isSuccessful)
                FromNetwork.Success(body = response.body()?.items!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())
        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }
    }

    override suspend fun getUserDetail(username: String): FromNetwork<UserResponse> {
        return try {
            val response = apiService.getUserDetail(username)
            return if (response.isSuccessful)
                FromNetwork.Success(body = response.body()!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())
        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }
    }

    override suspend fun getUserFollower(username: String): FromNetwork<List<UserResponse>> {
        return try {
            val response = apiService.getUserFollower(username)
            return if (response.isSuccessful)
                FromNetwork.Success(body = response.body()!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())

        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }
    }

    override suspend fun getUserFollowing(username: String): FromNetwork<List<UserResponse>> {
        return try {
            val response = apiService.getUserFollowing(username)
            return if (response.isSuccessful)
                FromNetwork.Success(body = response.body()!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())
        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }
    }

}

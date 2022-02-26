package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.FromNetwork
import com.onirutla.githubuser.data.api.ApiInterface
import com.onirutla.githubuser.data.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : Repository {

    override suspend fun findUsersByUsername(username: String): FromNetwork<List<UserResponse>> =
        try {
            val response = apiInterface.findUsersByUsername(username)
            if (response.isSuccessful)
                FromNetwork.Success(body = response.body()?.items!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())
        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }

    override suspend fun getUserDetail(username: String): FromNetwork<UserResponse> =
        try {
            val response = apiInterface.getUserDetail(username)
            if (response.isSuccessful)
                FromNetwork.Success(body = response.body()!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())
        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }

    override suspend fun getUserFollowers(username: String): FromNetwork<List<UserResponse>> =
        try {
            val response = apiInterface.getUserFollowers(username)
            if (response.isSuccessful)
                FromNetwork.Success(body = response.body()!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())
        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }

    override suspend fun getUserFollowings(username: String): FromNetwork<List<UserResponse>> =
        try {
            val response = apiInterface.getUserFollowings(username)
            if (response.isSuccessful)
                FromNetwork.Success(body = response.body()!!, message = response.message())
            else
                FromNetwork.Error(message = response.message())
        } catch (t: Throwable) {
            FromNetwork.Error(message = t.message)
        }
}

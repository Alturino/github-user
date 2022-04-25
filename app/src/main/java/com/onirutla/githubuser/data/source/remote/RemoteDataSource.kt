package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.response.UserResponse

interface RemoteDataSource {
    suspend fun searchBy(username: String): NetworkState<List<UserResponse>>

    suspend fun getDetailBy(username: String): NetworkState<UserResponse>

    suspend fun getFollowerBy(username: String): NetworkState<List<UserResponse>>

    suspend fun getFollowingBy(username: String): NetworkState<List<UserResponse>>
}

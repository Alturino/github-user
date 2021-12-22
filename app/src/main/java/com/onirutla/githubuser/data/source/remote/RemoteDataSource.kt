package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.response.UserResponse

interface RemoteDataSource {
    suspend fun getUserSearch(username: String): NetworkState<List<UserResponse>>

    suspend fun getUserDetail(username: String): NetworkState<UserResponse>

    suspend fun getUserFollower(username: String): NetworkState<List<UserResponse>>

    suspend fun getUserFollowing(username: String): NetworkState<List<UserResponse>>
}

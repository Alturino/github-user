package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.source.remote.response.UserResponse

interface RemoteDataSource {
    suspend fun getUserSearch(username: String): FromNetwork<List<UserResponse>>

    suspend fun getUserDetail(username: String): FromNetwork<UserResponse>

    suspend fun getUserFollower(username: String): FromNetwork<List<UserResponse>>

    suspend fun getUserFollowing(username: String): FromNetwork<List<UserResponse>>
}

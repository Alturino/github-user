package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.FromNetwork
import com.onirutla.githubuser.data.response.SearchResponse
import com.onirutla.githubuser.data.response.UserResponse

interface Repository {

    suspend fun findUsersByUsername(username: String): FromNetwork<List<UserResponse>>
    suspend fun getUserDetail(username: String): FromNetwork<UserResponse>
    suspend fun getUserFollowers(username: String): FromNetwork<List<UserResponse>>
    suspend fun getUserFollowings(username: String): FromNetwork<List<UserResponse>>

}

package com.onirutla.githubuser.data.remote

import com.onirutla.githubuser.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

     suspend fun findUsersByUsername(username: String): List<UserResponse>
     suspend fun getUserDetail(username: String): UserResponse
     suspend fun getUserFollowers(username: String): List<UserResponse>
     suspend fun getUserFollowings(username: String): List<UserResponse>

}

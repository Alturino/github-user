package com.onirutla.githubuser.data.remote

import com.onirutla.githubuser.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

     fun findUsersByUsername(username: String): Flow<List<UserResponse>>
     fun getUserDetail(username: String): Flow<UserResponse>
     fun getUserFollowers(username: String): Flow<List<UserResponse>>
     fun getUserFollowings(username: String): Flow<List<UserResponse>>

}

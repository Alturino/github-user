package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.remote.FromNetwork
import com.onirutla.githubuser.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface Repository {

     fun findUsersByUsername(username: String): Flow<FromNetwork<List<UserResponse>>>
     fun getUserDetail(username: String): Flow<FromNetwork<UserResponse>>
     fun getUserFollowers(username: String): Flow<FromNetwork<List<UserResponse>>>
     fun getUserFollowings(username: String): Flow<FromNetwork<List<UserResponse>>>

}

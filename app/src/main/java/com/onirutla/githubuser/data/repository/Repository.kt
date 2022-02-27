package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.FromNetwork
import com.onirutla.githubuser.data.response.SearchResponse
import com.onirutla.githubuser.data.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface Repository {

     fun findUsersByUsername(username: String): Flow<FromNetwork<List<UserResponse>>>
     fun getUserDetail(username: String): Flow<FromNetwork<UserResponse>>
     fun getUserFollowers(username: String): Flow<FromNetwork<List<UserResponse>>>
     fun getUserFollowings(username: String): Flow<FromNetwork<List<UserResponse>>>

}

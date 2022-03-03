package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun findUserByUsername(username: String): Flow<Resource<List<UserEntity>>>
    fun getUserDetail(username: String): Flow<Resource<UserEntity>>
    fun getUserFollowers(username: String): Flow<Resource<List<UserEntity>>>
    fun getUserFollowings(username: String): Flow<Resource<List<UserEntity>>>
    fun getFavoriteUsers(): Flow<Resource<List<UserEntity>>>

}

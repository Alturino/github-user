package com.onirutla.githubuser.data.source

import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getUsersSearch(username: String): Flow<Resource<List<UserEntity>>>

    fun getUserDetail(username: String): Flow<Resource<UserEntity>>

    fun getUsersFollower(username: String): Flow<Resource<List<UserEntity>>>

    fun getUsersFollowing(username: String): Flow<Resource<List<UserEntity>>>

    fun getUsersFavorite(): Flow<Resource<List<UserEntity>>>

    suspend fun setUserFavorite(userDto: UserEntity): UserEntity
}
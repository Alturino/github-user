package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsersSearch(username: String): Flow<Resource<List<UserDTO>>>

    fun getUserDetail(username: String): Flow<Resource<UserDTO>>

    fun getUsersFollower(username: String): Flow<Resource<List<UserDTO>>>

    fun getUsersFollowing(username: String): Flow<Resource<List<UserDTO>>>

    fun getUsersFavorite(): Flow<Resource<List<UserDTO>>>

    suspend fun setUserFavorite(userDto: UserDTO): UserEntity
}

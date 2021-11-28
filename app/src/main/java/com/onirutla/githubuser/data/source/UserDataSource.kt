package com.onirutla.githubuser.data.source

import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getUsersSearch(username: String): Flow<Resource<List<UserDTO>>>

    fun getUserDetail(username: String): Flow<Resource<UserDTO>>

    fun getUsersFollower(username: String): Flow<Resource<List<UserDTO>>>

    fun getUsersFollowing(username: String): Flow<Resource<List<UserDTO>>>

    fun getUsersFavorite(): Flow<Resource<List<UserDTO>>>

    suspend fun setUserFavorite(userDto: UserDTO)
}
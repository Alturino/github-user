package com.onirutla.githubuser.data.local

import com.onirutla.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun findUserByUsername(username: String): Flow<List<UserEntity>>
    fun getFavoriteUsers(): Flow<List<UserEntity>>
    fun getUserDetail(username: String): Flow<UserEntity?>
    suspend fun insertUser(user: UserEntity)
    suspend fun insertUsers(users: List<UserEntity>)
    suspend fun favorite(userEntity: UserEntity): UserEntity
    suspend fun unFavorite(userEntity: UserEntity): UserEntity

}

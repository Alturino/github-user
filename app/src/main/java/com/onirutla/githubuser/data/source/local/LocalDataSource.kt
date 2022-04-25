package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun searchBy(username: String): Flow<List<UserEntity>>

    fun getFavorite(): Flow<List<UserEntity>>

    fun getDetailBy(username: String): Flow<UserEntity>

    suspend fun insertUsers(users: List<UserEntity>)

    suspend fun insertUserDetail(userEntity: UserEntity)

    suspend fun favorite(userEntity: UserEntity): UserEntity

    suspend fun unFavorite(userEntity: UserEntity): UserEntity

}

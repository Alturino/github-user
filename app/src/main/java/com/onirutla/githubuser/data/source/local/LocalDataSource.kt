package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getUserSearch(username: String): Flow<FromDb<List<UserEntity>>>

    fun getFavorite(): Flow<FromDb<List<UserEntity>>>

    fun getUserDetail(username: String): Flow<FromDb<UserEntity>>

    suspend fun insertUsers(users: List<UserEntity>)

    suspend fun insertUserDetail(userEntity: UserEntity)

    suspend fun favorite(userEntity: UserEntity): UserEntity

    suspend fun unFavorite(userEntity: UserEntity): UserEntity

}

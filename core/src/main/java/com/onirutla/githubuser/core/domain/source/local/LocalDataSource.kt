package com.onirutla.githubuser.core.domain.source.local

import com.onirutla.githubuser.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

internal interface LocalDataSource {

    fun searchBy(username: String): Flow<List<UserEntity>>

    fun getFavorite(): Flow<List<UserEntity>>

    fun getDetailBy(username: String): Flow<UserEntity>

    suspend fun insertUsers(vararg users: UserEntity)

    suspend fun favorite(userEntity: UserEntity): UserEntity

    suspend fun unFavorite(userEntity: UserEntity): UserEntity

}

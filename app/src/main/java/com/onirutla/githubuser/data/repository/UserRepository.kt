package com.onirutla.githubuser.data.repository

import androidx.paging.PagingData
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun searchByPaging(username: String): Flow<PagingData<UserEntity>>

    fun getDetailBy(username: String): Flow<Resource<UserEntity>>

    fun getFollowerPaging(username: String): Flow<PagingData<UserEntity>>

    fun getFollowingPaging(username: String): Flow<PagingData<UserEntity>>

    fun getFavorite(): Flow<Resource<List<UserEntity>>>

    suspend fun setFavorite(userDto: UserEntity): UserEntity
}
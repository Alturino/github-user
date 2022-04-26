package com.onirutla.githubuser.data.repository

import androidx.paging.PagingData
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun searchBy(username: String): Flow<Resource<List<UserEntity>>>

    fun getDetailBy(username: String): Flow<Resource<UserEntity>>

    fun getFollowerBy(username: String): Flow<Resource<List<UserEntity>>>
    fun getFollowerPaging(username: String): Flow<PagingData<UserEntity>>

    fun getFollowingBy(username: String): Flow<Resource<List<UserEntity>>>
    fun getFollowingPaging(username: String): Flow<PagingData<UserEntity>>

    fun getFavorite(): Flow<Resource<List<UserEntity>>>

    suspend fun setUserFavorite(userDto: UserEntity): UserEntity
}
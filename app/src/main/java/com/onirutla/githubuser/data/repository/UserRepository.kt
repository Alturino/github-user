package com.onirutla.githubuser.data.repository

import androidx.paging.PagingData
import com.onirutla.githubuser.data.UIState
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun searchByPaging(username: String): Flow<PagingData<UserEntity>>

    fun getDetailBy(username: String): Flow<UIState<UserEntity>>

    fun getFollowerPaging(username: String): Flow<PagingData<UserEntity>>

    fun getFollowingPaging(username: String): Flow<PagingData<UserEntity>>

    fun getFavorite(): Flow<UIState<List<UserEntity>>>

    suspend fun setFavorite(userDto: UserEntity): UserEntity
}
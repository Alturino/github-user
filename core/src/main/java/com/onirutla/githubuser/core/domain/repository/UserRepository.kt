package com.onirutla.githubuser.core.domain.repository

import androidx.paging.PagingData
import com.onirutla.githubuser.core.data.UIState
import com.onirutla.githubuser.core.domain.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun searchByPaging(username: String): Flow<PagingData<User>>

    fun getDetailBy(username: String): Flow<UIState<User>>

    fun getFollowerPaging(username: String): Flow<PagingData<User>>

    fun getFollowingPaging(username: String): Flow<PagingData<User>>

    fun getFavorite(): Flow<UIState<List<User>>>

    suspend fun setFavorite(userDto: User): User
}
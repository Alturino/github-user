package com.onirutla.githubuser.data.source.remote

import androidx.paging.PagingData
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun searchBy(username: String, position: Int): Response<List<UserResponse>>

    suspend fun getDetailBy(username: String): Response<UserResponse>

    fun getFollowerPaging(username: String): Flow<PagingData<UserEntity>>

    fun getFollowingPaging(username: String): Flow<PagingData<UserEntity>>
}

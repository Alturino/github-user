package com.onirutla.githubuser.data.source.remote

import androidx.paging.PagingData
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun searchBy(username: String): Response<List<UserResponse>>

    suspend fun getDetailBy(username: String): Response<UserResponse>

    suspend fun getFollowerBy(username: String): Response<List<UserResponse>>
    fun getFollowerPaging(username: String): Flow<PagingData<UserEntity>>

    suspend fun getFollowingBy(username: String): Response<List<UserResponse>>
}

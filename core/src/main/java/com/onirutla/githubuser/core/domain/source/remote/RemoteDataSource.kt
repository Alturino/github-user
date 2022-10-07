package com.onirutla.githubuser.core.domain.source.remote

import androidx.paging.PagingData
import com.onirutla.githubuser.core.data.source.remote.NetworkResponse
import com.onirutla.githubuser.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

internal interface RemoteDataSource {

    suspend fun searchBy(username: String, position: Int): NetworkResponse<List<UserResponse>>

    suspend fun getDetailBy(username: String): NetworkResponse<UserResponse>

    fun getFollowerPaging(username: String): Flow<PagingData<UserResponse>>

    fun getFollowingPaging(username: String): Flow<PagingData<UserResponse>>

}

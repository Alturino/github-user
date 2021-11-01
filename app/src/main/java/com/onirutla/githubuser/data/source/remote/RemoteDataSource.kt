package com.onirutla.githubuser.data.source.remote

import androidx.lifecycle.liveData
import com.onirutla.githubuser.data.source.remote.network.NetworkService
import com.onirutla.githubuser.data.source.remote.response.SearchResponse
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: NetworkService) {

    suspend fun getUserSearch(username: String): SearchResponse {
        val response = apiService.getUsersSearch(username)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            throw Throwable()
        }
    }

    suspend fun getUserDetail(username: String): UserResponse {
        val response = apiService.getUserDetail(username = username)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            throw Throwable()
        }
    }

    fun getUserFollower(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUserFollower(username)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }

    fun getUserFollowing(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUserFollowing(username)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }

}
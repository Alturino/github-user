package com.onirutla.githubuser.data.remote

import androidx.lifecycle.liveData
import com.onirutla.githubuser.data.remote.network.NetworkService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: NetworkService) {

    fun getUserSearch(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUsersSearch(username = username)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }

    fun getUserDetail(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUserDetail(username = username)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
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
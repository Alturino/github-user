package com.onirutla.githubuser.data.remote

import android.util.Log
import androidx.lifecycle.liveData
import com.onirutla.githubuser.data.remote.network.NetworkService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: NetworkService) {

    fun getUserSearch(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUsersSearch(username = username)
        Log.d("user search", "${response.body()?.items}")
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }

    fun getUserDetail(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUserDetail(username = username)
        Log.d("user detail", "${response.body()}")
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }

    fun getUserFollower(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUserFollower(username)
        Log.d("user follower", "${response.body()}")
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }

    fun getUserFollowing(username: String) = liveData(Dispatchers.IO) {
        val response = apiService.getUserFollowing(username)
        Log.d("user following", "${response.body()}")
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }
}
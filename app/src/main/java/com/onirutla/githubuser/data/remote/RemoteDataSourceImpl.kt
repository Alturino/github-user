package com.onirutla.githubuser.data.remote

import android.util.Log
import com.onirutla.githubuser.data.remote.api.ApiInterface
import com.onirutla.githubuser.data.remote.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiInterface: ApiInterface,
) : RemoteDataSource {

    override suspend fun findUsersByUsername(username: String): List<UserResponse> = try {
        val response = apiInterface.findUsersByUsername(username)
        Log.d("remote findUserByName", "${response.body()?.items}")
        Log.d("remote findUserByName", "${response.body()?.items!!.size}")
        if (response.isSuccessful)
            response.body()?.items!!
        else
            emptyList()
    } catch (e: Exception) {
        Log.d("remote findUser", "$e")
        emptyList()
    }

    override suspend fun getUserDetail(username: String): UserResponse = try {
        val response = apiInterface.getUserDetail(username)
        Log.d("remote getUserDetail", "${response.body()}")
        if (response.isSuccessful)
            response.body()!!
        else
            UserResponse()
    } catch (t: Throwable) {
        Log.d("remote userDetail", "$t")
        UserResponse()
    }

    override suspend fun getUserFollowers(username: String): List<UserResponse> = try {
        val response = apiInterface.getUserFollowers(username)
        Log.d("remote", "${response.body()}")
        Log.d("remote followers", "${response.body()?.size}")
        if (response.isSuccessful)
            response.body()!!
        else
            emptyList()
    } catch (t: Throwable) {
        Log.d("remote followers", "$t")
        emptyList()
    }

    override suspend fun getUserFollowings(username: String): List<UserResponse> = try {
        val response = apiInterface.getUserFollowings(username)
        Log.d("remote", "${response.body()}")
        Log.d("remote followings", "${response.body()?.size}")
        if (response.isSuccessful)
            response.body()!!
        else
            emptyList()
    } catch (t: Throwable) {
        Log.d("remote followers", "$t")
        emptyList()
    }
}

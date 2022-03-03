package com.onirutla.githubuser.data.remote

import android.util.Log
import com.onirutla.githubuser.data.remote.api.ApiInterface
import com.onirutla.githubuser.data.remote.response.UserResponse
import com.onirutla.githubuser.util.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource {

    override fun findUsersByUsername(username: String): Flow<List<UserResponse>> = flow {
        val response = apiInterface.findUsersByUsername(username)
        Log.d("remote findUserByName", "${response.body()?.items}")
        Log.d("remote findUserByName", "${response.body()?.items!!.size}")
        if (response.isSuccessful)
            emit(response.body()?.items!!)
        else
            Log.d("remote findUserByName", "${response.errorBody()}")
    }.catch {
        Log.d("remote findUserByName", "$it")
    }.flowOn(ioDispatcher).filterNotNull()

    override fun getUserDetail(username: String): Flow<UserResponse> = flow {
        val response = apiInterface.getUserDetail(username)
        Log.d("remote getUserDetail", "${response.body()}")
        if (response.isSuccessful)
            emit(response.body())
        else
            Log.d("remote getUserDetail", "${response.errorBody()}")
    }.catch {
        Log.d("remote getUserDetail", "$it")
    }.flowOn(ioDispatcher).filterNotNull()


    override fun getUserFollowers(username: String): Flow<List<UserResponse>> = flow {
        val response = apiInterface.getUserFollowers(username)
        Log.d("remote", "${response.body()}")
        Log.d("remote findUserByName", "${response.body()?.size}")
        if (response.isSuccessful)
            emit(response.body()!!)
        else
            Log.d("remote getUserFollower", "${response.errorBody()}")
    }.catch {
        Log.d("remote getUserFollower", "$it")
    }.flowOn(ioDispatcher).filterNotNull()

    override fun getUserFollowings(username: String): Flow<List<UserResponse>> = flow {
        val response = apiInterface.getUserFollowings(username)
        Log.d("remote", "${response.body()}")
        Log.d("remote findUserByName", "${response.body()?.size}")
        if (response.isSuccessful)
            emit(response.body()!!)
        else
            Log.d("remote getUserFollower", "${response.errorBody()}")
    }.catch {
        Log.d("remote getUserFollower", "$it")
    }.flowOn(ioDispatcher).filterNotNull()
}

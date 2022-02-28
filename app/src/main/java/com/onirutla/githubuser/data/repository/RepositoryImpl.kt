package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.FromNetwork
import com.onirutla.githubuser.data.api.ApiInterface
import com.onirutla.githubuser.data.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : Repository {

    override fun findUsersByUsername(username: String): Flow<FromNetwork<List<UserResponse>>> =
        flow {
            emit(FromNetwork.Loading())
            try {
                val response = apiInterface.findUsersByUsername(username)
                if (response.isSuccessful)
                    emit(
                        FromNetwork.Success(
                            data = response.body()?.items!!,
                            message = response.message()
                        )
                    )
                else
                    emit(FromNetwork.Error(message = response.message()))
            } catch (t: Throwable) {
                emit(FromNetwork.Error(message = t.message))
            }
        }

    override fun getUserDetail(username: String): Flow<FromNetwork<UserResponse>> = flow {
        emit(FromNetwork.Loading())
        try {
            val response = apiInterface.getUserDetail(username)
            if (response.isSuccessful)
                emit(FromNetwork.Success(data = response.body()!!, message = response.message()))
            else
                emit(FromNetwork.Error(message = response.message()))
        } catch (t: Throwable) {
            emit(FromNetwork.Error(message = t.message))
        }
    }

    override fun getUserFollowers(username: String): Flow<FromNetwork<List<UserResponse>>> =
        flow {
            emit(FromNetwork.Loading())
            try {
                val response = apiInterface.getUserFollowers(username)
                if (response.isSuccessful)
                    emit(
                        FromNetwork.Success(
                            data = response.body()!!,
                            message = response.message()
                        )
                    )
                else
                    emit(FromNetwork.Error(message = response.message()))
            } catch (t: Throwable) {
                emit(FromNetwork.Error(message = t.message))
            }
        }

    override fun getUserFollowings(username: String): Flow<FromNetwork<List<UserResponse>>> =
        flow {
            emit(FromNetwork.Loading())
            try {
                val response = apiInterface.getUserFollowings(username)
                if (response.isSuccessful)
                    emit(
                        FromNetwork.Success(
                            data = response.body()!!,
                            message = response.message()
                        )
                    )
                else
                    emit(FromNetwork.Error(message = response.message()))
            } catch (t: Throwable) {
                emit(FromNetwork.Error(message = t.message))
            }
        }
}

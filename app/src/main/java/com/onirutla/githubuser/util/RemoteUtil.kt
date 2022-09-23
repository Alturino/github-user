package com.onirutla.githubuser.util

import com.onirutla.githubuser.data.source.remote.NetworkResponse
import retrofit2.Response


inline fun <reified T> Response<T>.getResult(): NetworkResponse<T> {
    return try {
        if (this.isSuccessful)
            NetworkResponse.Success(this.body()!!)
        else
            NetworkResponse.Error(message = message())
    } catch (e: Exception) {
        NetworkResponse.Error(message = e.message)
    }
}

inline fun <reified T, R> Response<T>.getResult(function: Response<T>.() -> R): NetworkResponse<R> {
    return try {
        if (this.isSuccessful)
            NetworkResponse.Success(function(this))
        else
            NetworkResponse.Error(message = message())
    } catch (e: Exception) {
        NetworkResponse.Error(message = e.message)
    }
}
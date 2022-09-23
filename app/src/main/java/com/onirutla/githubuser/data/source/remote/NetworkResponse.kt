package com.onirutla.githubuser.data.source.remote

sealed class NetworkResponse<T> {
    data class Success<T>(val body: T, val message: String? = null) : NetworkResponse<T>()
    data class Error<T>(val message: String? = null, val data: T? = null) : NetworkResponse<T>()
}
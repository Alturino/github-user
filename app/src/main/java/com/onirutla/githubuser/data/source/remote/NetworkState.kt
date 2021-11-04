package com.onirutla.githubuser.data.source.remote

sealed class NetworkState<T> {
    data class Success<T>(val body: T, val message: String?) : NetworkState<T>()
    data class Error<T>(val data: T? = null, val message: String?) : NetworkState<T>()
}
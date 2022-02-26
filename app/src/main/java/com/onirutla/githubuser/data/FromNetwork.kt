package com.onirutla.githubuser.data

sealed class FromNetwork<T> {
    data class Success<T>(val body: T, val message: String? = null) : FromNetwork<T>()
    data class Error<T>(val data: T? = null, val message: String? = null) : FromNetwork<T>()
}

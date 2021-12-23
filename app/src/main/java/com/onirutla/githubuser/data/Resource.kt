package com.onirutla.githubuser.data

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String?) : Resource<T>()
    class Loading<T> : Resource<T>()
}

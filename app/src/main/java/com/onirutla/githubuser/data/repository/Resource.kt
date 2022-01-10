package com.onirutla.githubuser.data.repository

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String? = null) : Resource<T>()
    data class Loading<T>(val data: T? = null) : Resource<T>()
}

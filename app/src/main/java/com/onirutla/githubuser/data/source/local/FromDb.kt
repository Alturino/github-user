package com.onirutla.githubuser.data.source.local

sealed class FromDb<T> {
    data class Success<T>(val data: T) : FromDb<T>()
    data class Empty<T>(val message: String? = null) : FromDb<T>()
}
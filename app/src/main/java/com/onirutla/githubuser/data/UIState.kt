package com.onirutla.githubuser.data

sealed class UIState<T> {
    data class Success<T>(val data: T) : UIState<T>()
    data class Error<T>(val message: String? = null) : UIState<T>()
    data class Loading<T>(val data: T? = null) : UIState<T>()
}

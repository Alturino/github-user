package com.onirutla.githubuser.data.source.remote

import com.onirutla.githubuser.data.UIState

sealed class NetworkResponse<T> {
    data class Success<T>(val body: T, val message: String? = null) : NetworkResponse<T>()
    data class Error<T>(val message: String? = null, val data: T? = null) : NetworkResponse<T>()
}

inline fun <reified T> NetworkResponse<T>.doWhenSuccess(function: NetworkResponse.Success<T>.() -> Unit) {
    if (this is NetworkResponse.Success)
        function.invoke(this)
}

inline fun <reified T> NetworkResponse<T>.doWhenError(function: NetworkResponse.Error<T>.() -> Unit) {
    if (this is NetworkResponse.Error)
        function.invoke(this)
}

inline fun <reified T, R> NetworkResponse<T>.getUiState(function: NetworkResponse.Success<T>.() -> UIState<R>): UIState<R> {
    return when (this) {
        is NetworkResponse.Error -> UIState.Error(this.message)
        is NetworkResponse.Success -> function.invoke(this)
    }
}
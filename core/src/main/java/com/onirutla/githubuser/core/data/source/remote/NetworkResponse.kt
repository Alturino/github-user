package com.onirutla.githubuser.core.data.source.remote

import com.onirutla.githubuser.core.data.UIState

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

inline fun <reified T, R> NetworkResponse<T>.mapToUiState(function: NetworkResponse.Success<T>.() -> UIState<R>): UIState<R> {
    return when (this) {
        is NetworkResponse.Error -> UIState.Error(this.message)
        is NetworkResponse.Success -> function.invoke(this)
    }
}

inline fun <reified T> NetworkResponse<T>.getData(function: NetworkResponse.Success<T>.() -> T): T? {
    return when (this) {
        is NetworkResponse.Error -> null
        is NetworkResponse.Success -> function(this)
    }
}
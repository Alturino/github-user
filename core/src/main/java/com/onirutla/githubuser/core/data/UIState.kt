package com.onirutla.githubuser.core.data

sealed class UIState<T> {
    data class Success<T>(val data: T) : UIState<T>()
    data class Error<T>(val message: String? = null) : UIState<T>()
    data class Loading<T>(val data: T? = null) : UIState<T>()
}

inline fun <reified T> UIState<T>.doWhenLoading(codeToExecute: UIState.Loading<T>.() -> Unit) {
    if (this is UIState.Loading)
        codeToExecute(this)
    return
}

inline fun <reified T> UIState<T>.doWhenSuccess(codeToExecute: UIState.Success<T>.() -> Unit) {
    if (this is UIState.Success)
        codeToExecute(this)
    return
}

inline fun <reified T> UIState<T>.doWhenError(codeToExecute: UIState.Error<T>.() -> Unit) {
    if (this is UIState.Error)
        codeToExecute(this)
    return
}

inline fun <reified T> UIState<T>.doWhen(
    error: UIState.Error<T>.() -> Unit,
    loading: UIState.Loading<T>.() -> Unit,
    success: UIState.Success<T>.() -> Unit,
) {
    when(this){
        is UIState.Error -> error(this)
        is UIState.Loading -> loading(this)
        is UIState.Success -> success(this)
    }
}


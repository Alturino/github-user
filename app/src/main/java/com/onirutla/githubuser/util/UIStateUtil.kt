package com.onirutla.githubuser.util

import com.onirutla.githubuser.data.UIState

inline fun <reified T> UIState<T>.doWhenLoading(codeToExecute: UIState.Loading<T>.() -> Unit) {
    if (this is UIState.Loading)
        codeToExecute()
    return
}

inline fun <reified T> UIState<T>.doWhenSuccess(codeToExecute: UIState.Success<T>.() -> Unit) {
    if (this is UIState.Success)
        codeToExecute()
    return
}

inline fun <reified T> UIState<T>.doWhenError(codeToExecute: UIState.Error<T>.() -> Unit) {
    if (this is UIState.Error)
        codeToExecute()
    return
}
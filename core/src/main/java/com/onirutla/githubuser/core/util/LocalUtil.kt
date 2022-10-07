package com.onirutla.githubuser.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun <reified T> Flow<List<T>>.ensureFlowOfListNotNull(
    context: CoroutineContext = EmptyCoroutineContext
): Flow<List<T>> = mapNotNull { it }
    .flowOn(context)


inline fun <reified T> Flow<T?>.ensureFlowNotNull(
    context: CoroutineContext = EmptyCoroutineContext
): Flow<T> = mapNotNull { it }
    .flowOn(context)

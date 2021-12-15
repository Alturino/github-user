package com.onirutla.githubuser.util

inline fun <I, O> mapList(input: List<I>, mapListItem: (I) -> O): List<O> {
    return input.map { mapListItem(it) }
}

inline fun <I, O> mapNullInputList(input: List<I>?, mapListItem: (I) -> O): List<O> {
    return input?.map { mapListItem(it) } ?: emptyList()
}

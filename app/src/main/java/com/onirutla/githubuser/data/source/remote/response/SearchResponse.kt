package com.onirutla.githubuser.data.source.remote.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "incomplete_results")
    var incompleteResults: Boolean? = null,

    @Json(name = "items")
    var items: List<UserResponse>? = null,

    @Json(name = "total_count")
    var totalCount: Int? = null
)
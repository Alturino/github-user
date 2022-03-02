package com.onirutla.githubuser.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(

    @Json(name = "incomplete_results")
    val incompleteResults: Boolean? = null,

    @Json(name = "items")
    val items: List<UserResponse>? = null,

    @Json(name = "total_count")
    val totalCount: Int? = null

)

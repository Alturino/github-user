package com.onirutla.githubuser.data.remote.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "incomplete_results")
    var incompleteResults: Boolean?,

    @Json(name = "items")
    var items: List<UserResponse>?,

    @Json(name = "total_count")
    var totalCount: Int?
)
package com.onirutla.githubuser.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "avatar_url")
    var avatarUrl: String? = "",

    @Json(name = "followers")
    var followers: Int? = 0,

    @Json(name = "followers_url")
    var followersUrl: String? = "",

    @Json(name = "following")
    var following: Int? = 0,

    @Json(name = "following_url")
    var followingUrl: String? = "",

    @Json(name = "id")
    var id: Int? = 0,

    @Json(name = "login")
    var username: String? = "",

    @Json(name = "name")
    var name: String? = "",

    @Json(name = "public_repos")
    var publicRepos: Int? = 0,

    @Json(name = "type")
    var type: String? = "",
)

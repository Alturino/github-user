package com.onirutla.githubuser.data.source.remote.response

import com.onirutla.githubuser.data.source.local.entity.UserEntity
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

fun UserResponse.toEntity() = UserEntity(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    username = this.username.orEmpty(),
    type = this.type.orEmpty(),
    followers = this.followers ?: 0,
    following = this.following ?: 0,
    followingUrl = this.followingUrl.orEmpty(),
    followersUrl = this.followersUrl.orEmpty(),
    publicRepos = this.publicRepos ?: 0,
    avatarUrl = this.avatarUrl.orEmpty(),
)

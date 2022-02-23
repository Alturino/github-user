package com.onirutla.githubuser.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Response(

    @Json(name = "users")
    val users: List<UserItem>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class UserItem(

    @Json(name = "follower")
    val follower: Int,

    @Json(name = "following")
    val following: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "company")
    val company: String,

    @Json(name = "location")
    val location: String,

    @Json(name = "avatar")
    val avatar: String,

    @Json(name = "repository")
    val repository: Int,

    @Json(name = "username")
    val username: String
) : Parcelable

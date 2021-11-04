package com.onirutla.githubuser.data

data class UserDTO(
    val id: Int,
    val name: String,
    val username: String?,
    val type: String,
    val followers: Int,
    val following: Int,
    val followingUrl: String,
    val followersUrl: String,
    val publicRepos: Int,
    val avatarUrl: String,
    val isFavorite: Boolean
)

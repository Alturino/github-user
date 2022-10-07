package com.onirutla.githubuser.core.domain.data

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val type: String,
    val followers: Int,
    val following: Int,
    val publicRepos: Int,
    val followersUrl: String,
    val followingUrl: String,
    val avatarUrl: String,
    val isFavorite: Boolean = false
)

package com.onirutla.githubuser.data.repository

data class UserDTO(
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

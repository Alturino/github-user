package com.onirutla.githubuser.data

import com.onirutla.githubuser.data.source.local.entity.UserEntity

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

fun UserDTO.toEntity() = UserEntity(
    id,
    username,
    name,
    type,
    followers,
    following,
    publicRepos,
    followersUrl,
    followingUrl,
    avatarUrl,
    isFavorite
)

package com.onirutla.githubuser.data

import com.onirutla.githubuser.data.source.local.entity.UserEntity

data class UserDTO(
    val id: Int,
    val name: String,
    val username: String,
    val type: String,
    val followers: Int,
    val following: Int,
    val followingUrl: String,
    val followersUrl: String,
    val publicRepos: Int,
    val avatarUrl: String,
    val isFavorite: Boolean
)

fun UserDTO.toEntity(): UserEntity = UserEntity(
    id = this.id,
    username = this.username,
    name = this.name,
    type = this.type,
    followers = this.followers,
    following = this.following,
    publicRepos = this.publicRepos,
    followersUrl = this.followersUrl,
    followingUrl = this.followingUrl,
    avatarUrl = this.avatarUrl,
    isFavorite = this.isFavorite
)
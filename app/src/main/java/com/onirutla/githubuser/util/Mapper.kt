package com.onirutla.githubuser.util

import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.response.UserResponse

fun UserResponse.toEntity() = UserEntity(
    id = id ?: 0,
    name = name.orEmpty(),
    username = username.orEmpty(),
    type = type.orEmpty(),
    followers = followers ?: 0,
    following = following ?: 0,
    followingUrl = followingUrl.orEmpty(),
    followersUrl = followersUrl.orEmpty(),
    publicRepos = publicRepos ?: 0,
    avatarUrl = avatarUrl.orEmpty(),
)

fun List<UserResponse>.toEntities() = map {
    it.toEntity()
}
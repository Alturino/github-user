package com.onirutla.githubuser.util

import com.onirutla.githubuser.data.repository.UserDTO
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.response.UserResponse

object Mapper {

    // User DTO

    fun UserDTO.toEntity() = UserEntity(
        id = id,
        username = username,
        name = name,
        type = type,
        followers = followers,
        following = following,
        publicRepos = publicRepos,
        followersUrl = followersUrl,
        followingUrl = followingUrl,
        avatarUrl = avatarUrl,
        isFavorite = isFavorite
    )

    // User Entity

    fun UserEntity.toDto() = UserDTO(
        id = id,
        username = username,
        name = name,
        type = type,
        followers = followers,
        following = following,
        publicRepos = publicRepos,
        followersUrl = followersUrl,
        followingUrl = followingUrl,
        avatarUrl = avatarUrl,
        isFavorite = isFavorite
    )

    // User Response

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

    fun UserResponse.toDto() = UserDTO(
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

}

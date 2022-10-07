package com.onirutla.githubuser.core.util

import androidx.paging.PagingData
import androidx.paging.map
import com.onirutla.githubuser.core.data.source.local.entity.UserEntity
import com.onirutla.githubuser.core.data.source.remote.response.UserResponse
import com.onirutla.githubuser.core.domain.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun UserResponse.toEntity() = UserEntity(
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

internal fun List<UserResponse>.responsesToEntities(): List<UserEntity> = map {
    it.toEntity()
}

internal fun UserResponse.toUser() = User(
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

internal fun List<UserResponse>.responseToDomains(): List<User> = map {
    it.toUser()
}

internal fun UserEntity.toUser() = User(
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

internal fun List<UserEntity>.entitiesToDomains() = map {
    it.toUser()
}

internal fun User.toEntity() = UserEntity(
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

internal fun List<User>.domainsToEntities(): List<UserEntity> = map {
    it.toEntity()
}

internal fun Flow<PagingData<UserEntity>>.entityToUser(): Flow<PagingData<User>> =
    map { pagingData ->
        pagingData.map {
            it.toUser()
        }
    }

internal fun Flow<PagingData<UserResponse>>.responseToUser() = map { pagingData ->
    pagingData.map {
        it.toUser()
    }
}
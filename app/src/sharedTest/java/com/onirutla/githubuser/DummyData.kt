package com.onirutla.githubuser

import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.response.SearchResponse
import com.onirutla.githubuser.data.source.remote.response.UserResponse

object DummyData {
    val userDtos: List<UserEntity> = listOf(
        UserEntity(
            1,
            "a",
            "a",
            "user",
            1,
            1,
            1,
            "a",
            "a",
            "a",
            false
        ),
        UserEntity(
            2,
            "a",
            "a",
            "user",
            1,
            1,
            1,
            "a",
            "a",
            "a",
            false
        ),
        UserEntity(
            3,
            "a",
            "a",
            "user",
            1,
            1,
            1,
            "a",
            "a",
            "a",
            false
        )
    )

    val userResponses: List<UserResponse> = listOf(
        UserResponse(
            id = 1,
            username = "a",
            name = "a",
            type = "user",
            followers = 1,
            following = 1,
            publicRepos = 1,
            followersUrl = "a",
            followingUrl = "a",
            avatarUrl = "a",
        ),
        UserResponse(
            id = 2,
            username = "a",
            name = "a",
            type = "user",
            followers = 1,
            following = 1,
            publicRepos = 1,
            followersUrl = "a",
            followingUrl = "a",
            avatarUrl = "a",
        ),
        UserResponse(
            id = 3,
            username = "a",
            name = "a",
            type = "user",
            followers = 1,
            following = 1,
            publicRepos = 1,
            followersUrl = "a",
            followingUrl = "a",
            avatarUrl = "a",
        )
    )

    val userResponse = UserResponse(
        id = 1,
        username = "a",
        name = "a",
        type = "user",
        followers = 1,
        following = 1,
        publicRepos = 1,
        followersUrl = "a",
        followingUrl = "a",
        avatarUrl = "a",
    )

    val searchResponse = SearchResponse(items = userResponses)

    val userEntities: List<UserEntity> = listOf(
        UserEntity(
            id = 1,
            username = "a",
            name = "a",
            type = "user",
            followers = 1,
            following = 1,
            publicRepos = 1,
            followersUrl = "a",
            followingUrl = "a",
            avatarUrl = "a",
        ),
        UserEntity(
            id = 2,
            username = "a",
            name = "a",
            type = "user",
            followers = 1,
            following = 1,
            publicRepos = 1,
            followersUrl = "a",
            followingUrl = "a",
            avatarUrl = "a",
        ),
        UserEntity(
            id = 3,
            username = "a",
            name = "a",
            type = "user",
            followers = 1,
            following = 1,
            publicRepos = 1,
            followersUrl = "a",
            followingUrl = "a",
            avatarUrl = "a",
        )
    )

    val userEntity = UserEntity(
        id = 1,
        username = "a",
        name = "a",
        type = "user",
        followers = 1,
        following = 1,
        publicRepos = 1,
        followersUrl = "a",
        followingUrl = "a",
        avatarUrl = "a",
        isFavorite = false
    )

    val favoriteDtos: List<UserEntity> = listOf(
        UserEntity(
            1,
            "a",
            "a",
            "user",
            1,
            1,
            1,
            "a",
            "a",
            "a",
            true
        ),
        UserEntity(
            2,
            "aa",
            "aa",
            "user",
            1,
            1,
            1,
            "aa",
            "aa",
            "aa",
            false
        ),
        UserEntity(
            3,
            "aaa",
            "aaa",
            "user",
            1,
            1,
            1,
            "aaa",
            "aaa",
            "aaa",
            false
        ),
        UserEntity(
            4,
            "a",
            "a",
            "user",
            1,
            1,
            1,
            "a",
            "a",
            "a",
            true
        )
    )

    val favorites: List<UserEntity> = listOf(
        UserEntity(
            1,
            "a",
            "a",
            "user",
            1,
            1,
            1,
            "a",
            "a",
            "a",
            true
        ),
        UserEntity(
            2,
            "aa",
            "aa",
            "user",
            1,
            1,
            1,
            "aa",
            "aa",
            "aa",
            false
        ),
        UserEntity(
            3,
            "aaa",
            "aaa",
            "user",
            1,
            1,
            1,
            "aaa",
            "aaa",
            "aaa",
            false
        ),
        UserEntity(
            4,
            "a",
            "a",
            "user",
            1,
            1,
            1,
            "a",
            "a",
            "a",
            true
        )
    )

    val dto: UserEntity = UserEntity(
        1,
        "a",
        "a",
        "user",
        1,
        1,
        1,
        "a",
        "a",
        "a",
        false
    )

    val favorite = UserEntity(
        1,
        "a",
        "a",
        "user",
        1,
        1,
        1,
        "a",
        "a",
        "a",
        true
    )

    val unFavorite = UserEntity(
        1,
        "a",
        "a",
        "user",
        1,
        1,
        1,
        "a",
        "a",
        "a",
        false
    )

}
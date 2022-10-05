package com.onirutla.githubuser.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
internal data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo
    val id: Int = 0,

    @ColumnInfo
    val username: String = "",

    @ColumnInfo
    val name: String = "",

    @ColumnInfo
    val type: String = "",

    @ColumnInfo
    val followers: Int = 0,

    @ColumnInfo
    val following: Int = 0,

    @ColumnInfo(name = "public_repos")
    val publicRepos: Int = 0,

    @ColumnInfo(name = "follower_url")
    val followersUrl: String = "",

    @ColumnInfo(name = "following_url")
    val followingUrl: String = "",

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String = "",

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)
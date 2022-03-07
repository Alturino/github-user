package com.onirutla.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo
    val id: Int,

    @ColumnInfo
    val username: String,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val type: String,

    @ColumnInfo
    val followers: Int,

    @ColumnInfo
    val following: Int,

    @ColumnInfo(name = "public_repos")
    val publicRepos: Int,

    @ColumnInfo
    val location: String,

    @ColumnInfo
    val company: String,

    @ColumnInfo(name = "follower_url")
    val followersUrl: String,

    @ColumnInfo(name = "following_url")
    val followingUrl: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)
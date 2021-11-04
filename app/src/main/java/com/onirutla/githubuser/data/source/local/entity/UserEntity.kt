package com.onirutla.githubuser.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onirutla.githubuser.data.UserDTO

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

    @ColumnInfo(name = "follower_url")
    val followersUrl: String,

    @ColumnInfo(name = "following_url")
    val followingUrl: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false
)

fun UserEntity.toDto(): UserDTO {
    return UserDTO(
        id = this.id,
        name = this.name,
        username = this.username,
        type = this.type,
        followers = this.followers,
        following = this.following,
        publicRepos = this.publicRepos,
        avatarUrl = this.avatarUrl,
        followersUrl = this.followersUrl,
        followingUrl = this.followingUrl,
        isFavorite = this.isFavorite
    )
}
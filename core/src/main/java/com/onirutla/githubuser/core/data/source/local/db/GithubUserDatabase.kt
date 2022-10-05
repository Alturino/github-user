package com.onirutla.githubuser.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onirutla.githubuser.core.data.source.local.dao.UserRemoteKeyDao
import com.onirutla.githubuser.core.data.source.local.dao.UserDao
import com.onirutla.githubuser.core.data.source.local.entity.UserEntity
import com.onirutla.githubuser.core.data.source.local.entity.UserRemoteKey

@Database(entities = [UserEntity::class, UserRemoteKey::class], version = 1, exportSchema = false)
internal abstract class GithubUserDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val userRemoteKeyDao: UserRemoteKeyDao
}
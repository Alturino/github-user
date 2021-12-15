package com.onirutla.githubuser.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
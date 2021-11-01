package com.onirutla.githubuser.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onirutla.githubuser.data.local.dao.UserDao
import com.onirutla.githubuser.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
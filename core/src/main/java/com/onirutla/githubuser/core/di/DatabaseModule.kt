package com.onirutla.githubuser.core.di

import android.content.Context
import androidx.room.Room
import com.onirutla.githubuser.core.data.source.local.dao.UserDao
import com.onirutla.githubuser.core.data.source.local.db.GithubUserDatabase
import com.onirutla.githubuser.core.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): GithubUserDatabase =
        Room.databaseBuilder(
            context,
            GithubUserDatabase::class.java,
            Constant.DB_NAME
        ).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesUserDao(db: GithubUserDatabase): UserDao = db.userDao
}
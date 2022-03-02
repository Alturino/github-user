package com.onirutla.githubuser.di

import android.content.Context
import androidx.room.Room
import com.onirutla.githubuser.data.local.AppDatabase
import com.onirutla.githubuser.data.local.UserDao
import com.onirutla.githubuser.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object DatabaseModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {
        @Singleton
        @Provides
        fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                Constant.DB_NAME
            ).fallbackToDestructiveMigration()
                .build()

        @Singleton
        @Provides
        fun providesUserDao(db: AppDatabase): UserDao = db.userDao
    }
}

package com.onirutla.githubuser.di

import android.content.Context
import com.onirutla.githubuser.data.preference.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context) = UserDataStore(context)

}

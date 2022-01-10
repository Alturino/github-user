package com.onirutla.githubuser.di

import android.content.Context
import com.onirutla.githubuser.data.repository.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun providesDataStoreManager(@ApplicationContext context: Context) = DataStoreManager(context)
}

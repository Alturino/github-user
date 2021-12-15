package com.onirutla.githubuser.di

import com.onirutla.githubuser.data.repository.UserRepository
import com.onirutla.githubuser.data.source.UserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindDataSource(repository: UserRepository): UserDataSource
}
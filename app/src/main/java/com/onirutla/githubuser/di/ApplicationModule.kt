package com.onirutla.githubuser.di

import com.onirutla.githubuser.data.repository.Repository
import com.onirutla.githubuser.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

}

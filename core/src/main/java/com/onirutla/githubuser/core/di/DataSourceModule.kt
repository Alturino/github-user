package com.onirutla.githubuser.core.di

import androidx.paging.ExperimentalPagingApi
import com.onirutla.githubuser.core.data.repository.UserRepositoryImpl
import com.onirutla.githubuser.core.domain.repository.UserRepository
import com.onirutla.githubuser.core.domain.source.local.LocalDataSource
import com.onirutla.githubuser.core.data.source.local.LocalDataSourceImpl
import com.onirutla.githubuser.core.domain.source.remote.RemoteDataSource
import com.onirutla.githubuser.core.data.source.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@ExperimentalPagingApi
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindDataSource(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource
}

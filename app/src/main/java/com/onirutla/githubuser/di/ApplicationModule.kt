package com.onirutla.githubuser.di

import com.onirutla.githubuser.data.local.LocalDataSource
import com.onirutla.githubuser.data.local.LocalDataSourceImpl
import com.onirutla.githubuser.data.remote.RemoteDataSource
import com.onirutla.githubuser.data.remote.RemoteDataSourceImpl
import com.onirutla.githubuser.data.repository.UserRepository
import com.onirutla.githubuser.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

}

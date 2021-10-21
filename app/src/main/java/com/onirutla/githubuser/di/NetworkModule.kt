package com.onirutla.githubuser.di

import com.onirutla.githubuser.data.remote.network.NetworkService
import com.onirutla.githubuser.util.Constant.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideMoshiBuilder(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(provideMoshiBuilder()))
            .baseUrl(BASE_URL)
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NetworkService =
        retrofit.create(NetworkService::class.java)
}

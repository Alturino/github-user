package com.onirutla.githubuser.data.repository

import android.util.Log
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.local.LocalDataSource
import com.onirutla.githubuser.data.local.entity.UserEntity
import com.onirutla.githubuser.data.remote.RemoteDataSource
import com.onirutla.githubuser.data.remote.response.toEntity
import com.onirutla.githubuser.util.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UserRepository {

    override fun findUserByUsername(username: String): Flow<Resource<List<UserEntity>>> = flow {
        localDataSource.findUserByUsername(username).collect { local ->
            if (local.isNullOrEmpty()) {
                remoteDataSource.findUsersByUsername(username).collect { remote ->
                    if (remote.isNullOrEmpty()) emit(Resource.Empty("data is not found"))

                    if (remote.isNotEmpty()) {
                        val caches = remote.map { it.toEntity() }
                        localDataSource.insertUsers(caches)

                        localDataSource.findUserByUsername(username).collect {
                            emit(Resource.Success(it))
                        }
                    }
                }
            }
            emit(Resource.Success(local))
        }
    }.onStart {
        emit(Resource.Loading())
    }.onEmpty {
        emit(Resource.Empty("data is not found"))
    }.catch {
        Log.d("repo findUser", "$it")
    }.flowOn(dispatcher)

    override fun getUserDetail(username: String): Flow<Resource<UserEntity>> = flow {
        localDataSource.getUserDetail(username).collect { local ->
            if (local == null || local.name == "") {
                remoteDataSource.getUserDetail(username).collect { remote ->
                    if (remote == null) {
                        emit(Resource.Empty("data is not found"))
                    }

                    if (remote != null) {
                        val cache = remote.toEntity()
                        localDataSource.insertUser(cache)
                        localDataSource.getUserDetail(username).collect {
                            emit(Resource.Success(it!!))
                        }
                    }
                }
            } else emit(Resource.Success(local))
        }
    }.onStart {
        emit(Resource.Loading())
    }.onEmpty {
        emit(Resource.Empty("data is not found"))
    }.catch {
        Log.d("repo userDetail", "$it")
    }.flowOn(dispatcher)

    override fun getUserFollowers(username: String): Flow<Resource<List<UserEntity>>> =
        remoteDataSource.getUserFollowers(username).map { remote ->
            if (remote.isNotEmpty()) {
                val caches = remote.map { it.toEntity() }
                Resource.Success(caches)
            } else
                Resource.Empty("data is not found")
        }.onStart {
            emit(Resource.Loading())
        }.onEmpty {
            emit(Resource.Empty("data is not found"))
        }.catch {
            Log.d("repo followers", "$it")
        }.flowOn(dispatcher)

    override fun getUserFollowings(username: String): Flow<Resource<List<UserEntity>>> =
        remoteDataSource.getUserFollowings(username).map { remote ->
            if (remote.isNotEmpty()) {
                val caches = remote.map { it.toEntity() }
                Resource.Success(caches)
            } else Resource.Empty("data is not found")
        }.onStart {
            emit(Resource.Loading())
        }.onEmpty {
            emit(Resource.Empty("data is not found"))
        }.catch {
            Log.d("repo followings", "$it")
        }.flowOn(dispatcher)

    override fun getFavoriteUsers(): Flow<Resource<List<UserEntity>>> =
        localDataSource.getFavoriteUsers().map {
            if (it.isNullOrEmpty())
                Resource.Empty("data is not found")
            else
                Resource.Success(it)
        }.onStart {
            emit(Resource.Loading())
        }.onEmpty {
            emit(Resource.Empty("data is not found"))
        }.catch {
            Log.d("repo ", "$it")
        }.flowOn(dispatcher)
}

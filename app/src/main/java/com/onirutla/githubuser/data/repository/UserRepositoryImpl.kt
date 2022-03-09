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

    override fun findUserByUsername(username: String): Flow<Resource<List<UserEntity>>> =
        localDataSource.findUsersByUsername(username).mapNotNull { fromDb ->
            if (fromDb.isNullOrEmpty()) {
                val fromNetwork =
                    remoteDataSource.findUsersByUsername(username).map { it.toEntity() }
                if (fromNetwork.isNullOrEmpty())
                    Resource.Empty()
                else {
                    localDataSource.insertUsers(fromNetwork)
                    Resource.Success(fromDb)
                }
            } else
                Resource.Success(fromDb)
        }.onStart {
            emit(Resource.Loading())
        }.onEmpty {
            emit(Resource.Empty("Data is not found"))
        }.catch {
            Log.d("repo findUser", "$it")
        }.flowOn(dispatcher)

    override fun getUserDetail(username: String): Flow<Resource<UserEntity>> =
        localDataSource.getUserDetail(username).mapNotNull { fromDb ->
            if (fromDb == null || fromDb.name == "") {
                val fromNetwork = remoteDataSource.getUserDetail(username).toEntity()
                if (fromNetwork == null)
                    Resource.Empty("Data is not found")
                else {
                    localDataSource.insertUser(fromNetwork)
                    Resource.Success(fromDb)
                }
            } else
                Resource.Success(fromDb)
        }.onStart {
            emit(Resource.Loading())
        }.onEmpty {
            emit(Resource.Empty("Data is not found"))
        }.catch {
            Log.d("repo findUser", "$it")
        }.flowOn(dispatcher)

    override fun getUserFollowers(username: String): Flow<Resource<List<UserEntity>>> = flow {
        val fromNetwork = remoteDataSource.getUserFollowers(username).map { it.toEntity() }
        if (fromNetwork.isEmpty())
            emit(Resource.Empty("Data is not found"))
        else
            emit(Resource.Success(fromNetwork))
    }.onStart {
        emit(Resource.Loading())
    }.onEmpty {
        emit(Resource.Empty("Data is not found"))
    }.catch {
        Log.d("repo findUser", "$it")
    }.flowOn(dispatcher)

    override fun getUserFollowings(username: String): Flow<Resource<List<UserEntity>>> = flow {
        val fromNetwork = remoteDataSource.getUserFollowings(username).map { it.toEntity() }
        if (fromNetwork.isEmpty())
            emit(Resource.Empty())
        else
            emit(Resource.Success(fromNetwork))
    }.onStart {
        emit(Resource.Loading())
    }.onEmpty {
        emit(Resource.Empty("Data is not found"))
    }.catch {
        Log.d("repo findUser", "$it")
    }.flowOn(dispatcher)

    override fun getFavoriteUsers(): Flow<Resource<List<UserEntity>>> =
        localDataSource.getFavoriteUsers().mapNotNull {
            if (it.isEmpty())
                Resource.Empty("Data is not found")
            else
                Resource.Success(it)
        }.onStart {
            emit(Resource.Loading())
        }

    override suspend fun setFavorite(user: UserEntity): UserEntity = when (user.isFavorite) {
        true -> localDataSource.unFavorite(user)
        false -> localDataSource.favorite(user)
    }
}

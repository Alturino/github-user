package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.NetworkState
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.response.toEntity
import com.onirutla.githubuser.util.mapNullInputList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : UserDataSource {

    override fun getUsersSearch(username: String): Flow<Resource<List<UserEntity>>> = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserSearch(username)) {
            is NetworkState.Success -> {
                val responseToEntities = mapNullInputList(networkState.body.items) {
                    it.toEntity()
                }

                val fromDb = localDataSource.getUserSearch(username)

                localDataSource.insertUsers(responseToEntities)

                //Maintain favorite state
                for (i in responseToEntities.indices) {
                    for (j in fromDb.indices) {
                        if (responseToEntities[i].id == fromDb[j].id && responseToEntities[i].isFavorite != fromDb[j].isFavorite) {
                            val temp = responseToEntities[i].copy(isFavorite = fromDb[j].isFavorite)
                            localDataSource.insertUserDetail(temp)
                        }
                    }
                }

                emit(Resource.Success(localDataSource.getUserSearch(username)))
            }
            is NetworkState.Error -> {

                val fromDb = localDataSource.getUserSearch(username)


                if (fromDb.isNullOrEmpty())
                    emit(Resource.Error(networkState.message))
                else
                    emit(Resource.Success(fromDb))
            }
        }
    }.flowOn(Dispatchers.IO)


    override fun getUserDetail(username: String): Flow<Resource<UserEntity>> = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserDetail(username)) {
            is NetworkState.Success -> {
                val fromNetwork = networkState.body.toEntity()

                val fromDb = localDataSource.getUserDetail(username)

                if (fromNetwork.id == fromDb.id && fromNetwork.isFavorite != fromDb.isFavorite) {
                    val temp = fromNetwork.copy(isFavorite = fromDb.isFavorite)
                    localDataSource.insertUserDetail(temp)
                    emit(Resource.Success(localDataSource.getUserDetail(username)))
                } else {
                    localDataSource.insertUserDetail(fromNetwork)
                    emit(Resource.Success(localDataSource.getUserDetail(username)))
                }
            }
            is NetworkState.Error -> {
                emit(Resource.Success(localDataSource.getUserDetail(username)))
            }
        }

    }.flowOn(Dispatchers.IO)


    override fun getUsersFollower(username: String): Flow<Resource<List<UserEntity>>> = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollower(username)) {
            is NetworkState.Success -> {
                val response = networkState.body

                // Map response to entity
                val entities = mapNullInputList(response) {
                    it.toEntity()
                }

                emit(Resource.Success(entities))
            }
            is NetworkState.Error -> {
                emit(Resource.Error(message = networkState.message))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getUsersFollowing(username: String): Flow<Resource<List<UserEntity>>> = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollowing(username)) {
            is NetworkState.Success -> {
                val response = networkState.body

                // Map response to entity
                val entities = mapNullInputList(response) {
                    it.toEntity()
                }

                // Insert to database
                localDataSource.insertUsers(entities)

                emit(Resource.Success(entities))
            }
            is NetworkState.Error -> {
                emit(Resource.Error(message = networkState.message))
            }
        }
    }.flowOn(Dispatchers.IO)


    override fun getUsersFavorite(): Flow<Resource<List<UserEntity>>> = flow {
        emit(Resource.Loading())

        localDataSource.getFavorite().collect {
            if (it.isNotEmpty())
                emit(Resource.Success(it))
            if (it.isNullOrEmpty())
                emit(Resource.Error("You don't have any favorite user yet"))
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun setUserFavorite(userEntity: UserEntity) {
        when (userEntity.isFavorite) {
            true -> localDataSource.unFavorite(userEntity)
            false -> localDataSource.favorite(userEntity)
        }
    }

}
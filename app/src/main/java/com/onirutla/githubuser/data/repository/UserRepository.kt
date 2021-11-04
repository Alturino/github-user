package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.local.entity.toDto
import com.onirutla.githubuser.data.source.remote.NetworkState
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.response.toDto
import com.onirutla.githubuser.data.source.remote.response.toEntity
import com.onirutla.githubuser.util.mapList
import com.onirutla.githubuser.util.mapNullInputList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : UserDataSource {

    override fun getUsersSearch(username: String): Flow<Resource<List<UserDTO>>> =
        flow {
            emit(Resource.Loading())


            when (val networkState = remoteDataSource.getUserSearch(username)) {
                is NetworkState.Success -> {
                    val entities = mapNullInputList(networkState.body.items) {
                        it.toEntity()
                    }

                    localDataSource.insertUsers(entities)

                    val dbToDto = mapList(localDataSource.getUserSearch(username)) {
                        it.toDto()
                    }

                    emit(Resource.Success(dbToDto))
                }
                is NetworkState.Error -> {

                    val dtos = mapList(localDataSource.getUserSearch(username)) {
                        it.toDto()
                    }

                    if (dtos.isNullOrEmpty())
                        emit(Resource.Error(networkState.message))
                    else
                        emit(Resource.Success(dtos))
                }
            }
        }.flowOn(Dispatchers.IO)


    override fun getUserDetail(username: String): Flow<Resource<UserDTO>> =
        flow {
            emit(Resource.Loading())

            when (val networkState = remoteDataSource.getUserDetail(username)) {
                is NetworkState.Success -> {
                    val entity = networkState.body.toEntity()

                    localDataSource.insertUserDetail(entity)

                    val fromDb = localDataSource.getUserDetail(username).toDto()

                    emit(Resource.Success(fromDb))
                }
                is NetworkState.Error -> {

                    val entity = localDataSource.getUserDetail(username)

                    emit(Resource.Success(entity.toDto()))
                }
            }
        }.flowOn(Dispatchers.IO)


    override fun getUsersFollower(username: String): Flow<Resource<List<UserDTO>>> =
        flow {
            emit(Resource.Loading())

            when (val networkState = remoteDataSource.getUserFollower(username)) {
                is NetworkState.Success -> {
                    val response = networkState.body

                    // Map response to entity
                    val entities = mapNullInputList(response) {
                        it.toEntity()
                    }

                    // Insert to database
                    localDataSource.insertUsers(entities)

                    // Map to Dto
                    val dtos = mapNullInputList(response) {
                        it.toDto()
                    }

                    emit(Resource.Success(dtos))
                }
                is NetworkState.Error -> {
                    emit(Resource.Error(message = networkState.message))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getUsersFollowing(username: String): Flow<Resource<List<UserDTO>>> =
        flow {
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

                    // Map to Dto
                    val dtos = mapNullInputList(entities) {
                        it.toDto()
                    }

                    emit(Resource.Success(dtos))
                }
                is NetworkState.Error -> {
                    emit(Resource.Error(message = networkState.message))
                }
            }
        }.flowOn(Dispatchers.IO)


    override fun getUsersFavorite(): Flow<Resource<List<UserDTO>>> = flow {
        emit(Resource.Loading())

        val dtos = mapList(localDataSource.getFavorite()) {
            it.toDto()
        }

        if (dtos.isNullOrEmpty())
            emit(Resource.Error(message = "You don't have any favorite user yet"))
        else
            emit(Resource.Success(dtos))

    }.flowOn(Dispatchers.IO)

    override suspend fun setUserFavorite(userEntity: UserEntity) {
        when (userEntity.isFavorite) {
            true -> localDataSource.unFavorite(userEntity)
            false -> localDataSource.favorite(userEntity)
        }
    }

}
package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.data.source.local.FromDb
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.local.entity.toDto
import com.onirutla.githubuser.data.source.remote.NetworkState
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.response.toDto
import com.onirutla.githubuser.data.source.remote.response.toEntity
import com.onirutla.githubuser.data.toEntity
import com.onirutla.githubuser.util.mapList
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
    private val localDataSource: LocalDataSource,
) : UserDataSource {

    override fun getUsersSearch(username: String): Flow<Resource<List<UserDTO>>> = flow {
        emit(Resource.Loading())

        localDataSource.getUserSearch(username).collect { fromDb ->
            when (fromDb) {
                is FromDb.Success -> {
                    val dtos = mapNullInputList(fromDb.data) { it.toDto() }
                    emit(Resource.Success(dtos))
                }
                is FromDb.Empty -> {
                    when (val networkState = remoteDataSource.getUserSearch(username)) {
                        is NetworkState.Success -> {
                            val fromNetwork = networkState.body.items

                            val cache = mapNullInputList(fromNetwork) { it.toEntity() }

                            localDataSource.insertUsers(cache)

                            val dto = mapNullInputList(fromNetwork) { it.toDto() }

                            emit(Resource.Success(dto))
                        }
                        is NetworkState.Error -> {
                            emit(Resource.Error<List<UserDTO>>(networkState.message))
                        }
                    }
                }
            }
        }

    }.flowOn(Dispatchers.IO)


    override fun getUserDetail(username: String): Flow<Resource<UserDTO>> = flow {
        emit(Resource.Loading())

        localDataSource.getUserDetail(username).collect { fromDb ->
            when (fromDb) {
                is FromDb.Empty -> {
                    when (val networkState = remoteDataSource.getUserDetail(username)) {
                        is NetworkState.Error -> emit(Resource.Error<UserDTO>(message = networkState.message))
                        is NetworkState.Success -> {
                            val fromNetwork = networkState.body.toEntity()

                            localDataSource.insertUserDetail(fromNetwork)
                            emit(Resource.Success(fromNetwork.toDto()))
                        }
                    }
                }
                is FromDb.Success -> emit(Resource.Success(fromDb.data.toDto()))
            }
        }

    }.flowOn(Dispatchers.IO)


    override fun getUsersFollower(username: String): Flow<Resource<List<UserDTO>>> = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollower(username)) {
            is NetworkState.Success -> {
                val fromNetwork = networkState.body

                val dto = mapNullInputList(fromNetwork) { it.toDto() }

                emit(Resource.Success(dto))
            }
            is NetworkState.Error -> emit(Resource.Error(message = networkState.message))
        }

    }.flowOn(Dispatchers.IO)

    override fun getUsersFollowing(username: String): Flow<Resource<List<UserDTO>>> = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollowing(username)) {
            is NetworkState.Success -> {
                val fromNetwork = networkState.body

                val dto = mapNullInputList(fromNetwork) { it.toDto() }

                emit(Resource.Success(dto))
            }
            is NetworkState.Error -> {
                emit(Resource.Error(message = networkState.message))
            }
        }
    }.flowOn(Dispatchers.IO)


    override fun getUsersFavorite(): Flow<Resource<List<UserDTO>>> = flow {
        emit(Resource.Loading())

        localDataSource.getFavorite().collect { entities ->
            when (entities) {
                is FromDb.Empty -> emit(Resource.Error<List<UserDTO>>("You don't have any favorite user yet"))
                is FromDb.Success -> {
                    val favorite = mapList(entities.data) { it.toDto() }

                    emit(Resource.Success(favorite))
                }
            }
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun setUserFavorite(userDto: UserDTO): UserEntity {
        return when (userDto.isFavorite) {
            true -> localDataSource.unFavorite(userDto.toEntity())
            false -> localDataSource.favorite(userDto.toEntity())
        }
    }

}
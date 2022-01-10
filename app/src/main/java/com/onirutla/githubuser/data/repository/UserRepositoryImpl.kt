package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.source.local.FromDb
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.FromNetwork
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.util.IoDispatcher
import com.onirutla.githubuser.util.Mapper.toDto
import com.onirutla.githubuser.util.Mapper.toEntity
import com.onirutla.githubuser.util.mapList
import com.onirutla.githubuser.util.mapNullInputList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override fun getUsersSearch(username: String) = flow<Resource<List<UserDTO>>> {
        emit(Resource.Loading())

        localDataSource.getUserSearch(username).collect { fromDb ->
            when (fromDb) {
                is FromDb.Success -> {
                    val dtos = mapNullInputList(fromDb.data) { it.toDto() }
                    emit(Resource.Success(dtos))
                }
                is FromDb.Empty -> {
                    when (val networkState = remoteDataSource.getUserSearch(username)) {
                        is FromNetwork.Success -> {
                            val fromNetwork = networkState.body

                            val cache = mapNullInputList(fromNetwork) { it.toEntity() }

                            localDataSource.insertUsers(cache)

                            val dto = mapNullInputList(fromNetwork) { it.toDto() }

                            emit(Resource.Success(dto))
                        }
                        is FromNetwork.Error -> {
                            emit(Resource.Error(networkState.message))
                        }
                    }
                }
            }
        }

    }.flowOn(ioDispatcher)

    override fun getUserDetail(username: String) = flow<Resource<UserDTO>> {
        emit(Resource.Loading())

        localDataSource.getUserDetail(username).collect { fromDb ->
            when (fromDb) {
                is FromDb.Empty -> {
                    when (val networkState = remoteDataSource.getUserDetail(username)) {
                        is FromNetwork.Error -> {
                            emit(Resource.Error(message = networkState.message))
                        }
                        is FromNetwork.Success -> {
                            val fromNetwork = networkState.body.toEntity()

                            localDataSource.insertUserDetail(fromNetwork)
                            emit(Resource.Success(fromNetwork.toDto()))
                        }
                    }
                }
                is FromDb.Success -> emit(Resource.Success(fromDb.data.toDto()))
            }
        }

    }.flowOn(ioDispatcher)


    override fun getUsersFollower(username: String) = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollower(username)) {
            is FromNetwork.Success -> {
                val fromNetwork = networkState.body

                val dto = mapNullInputList(fromNetwork) { it.toDto() }

                emit(Resource.Success(dto))
            }
            is FromNetwork.Error -> emit(Resource.Error(message = networkState.message))
        }

    }.flowOn(ioDispatcher)

    override fun getUsersFollowing(username: String) = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollowing(username)) {
            is FromNetwork.Success -> {
                val fromNetwork = networkState.body

                val dto = mapNullInputList(fromNetwork) { it.toDto() }

                emit(Resource.Success(dto))
            }
            is FromNetwork.Error -> {
                emit(Resource.Error(message = networkState.message))
            }
        }
    }.flowOn(ioDispatcher)


    override fun getUsersFavorite() = flow<Resource<List<UserDTO>>> {
        emit(Resource.Loading())

        localDataSource.getFavorite().collect { entities ->
            when (entities) {
                is FromDb.Empty -> emit(Resource.Error(message = entities.message))
                is FromDb.Success -> {
                    val favorite = mapList(entities.data) { it.toDto() }

                    emit(Resource.Success(favorite))
                }
            }
        }

    }.flowOn(ioDispatcher)

    override suspend fun setUserFavorite(userDto: UserDTO): UserEntity {
        return when (userDto.isFavorite) {
            true -> localDataSource.unFavorite(userDto.toEntity())
            false -> localDataSource.favorite(userDto.toEntity())
        }
    }

}

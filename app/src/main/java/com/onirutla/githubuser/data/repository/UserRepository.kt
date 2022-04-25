package com.onirutla.githubuser.data.repository

import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.NetworkState
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.response.toEntity
import com.onirutla.githubuser.util.IoDispatcher
import com.onirutla.githubuser.util.mapNullInputList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserDataSource {

    override fun getUsersSearch(username: String) = flow<Resource<List<UserEntity>>> {
        emit(Resource.Loading())


    }.flowOn(ioDispatcher)

    override fun getUserDetail(username: String) = flow<Resource<UserEntity>> {
        emit(Resource.Loading())


    }.flowOn(ioDispatcher)


    override fun getUsersFollower(username: String) = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollower(username)) {
            is NetworkState.Success -> {
                val fromNetwork = networkState.body

                val entity = mapNullInputList(fromNetwork) { it.toEntity() }

                emit(Resource.Success(entity))
            }
            is NetworkState.Error -> emit(Resource.Error(message = networkState.message))
        }

    }.flowOn(ioDispatcher)

    override fun getUsersFollowing(username: String) = flow {
        emit(Resource.Loading())

        when (val networkState = remoteDataSource.getUserFollowing(username)) {
            is NetworkState.Success -> {
                val fromNetwork = networkState.body

                val entity = mapNullInputList(fromNetwork) { it.toEntity() }

                emit(Resource.Success(entity))
            }
            is NetworkState.Error -> {
                emit(Resource.Error(message = networkState.message))
            }
        }
    }.flowOn(ioDispatcher)


    override fun getUsersFavorite() = flow<Resource<List<UserEntity>>> {
        emit(Resource.Loading())


    }.flowOn(ioDispatcher)

    override suspend fun setUserFavorite(userDto: UserEntity): UserEntity {
        return when (userDto.isFavorite) {
            true -> localDataSource.unFavorite(userDto)
            false -> localDataSource.favorite(userDto)
        }
    }

}

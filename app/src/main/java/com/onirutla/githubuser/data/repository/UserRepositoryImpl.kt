package com.onirutla.githubuser.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onirutla.githubuser.data.UIState
import com.onirutla.githubuser.data.source.UserRemoteMediator
import com.onirutla.githubuser.data.source.local.LocalDataSource
import com.onirutla.githubuser.data.source.local.db.GithubUserDatabase
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.remote.RemoteDataSource
import com.onirutla.githubuser.data.source.remote.mapToUiState
import com.onirutla.githubuser.util.Constant.GITHUB_PAGE_SIZE
import com.onirutla.githubuser.util.IoDispatcher
import com.onirutla.githubuser.util.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val database: GithubUserDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override fun searchByPaging(username: String): Flow<PagingData<UserEntity>> {
        return Pager(
            config = PagingConfig(pageSize = GITHUB_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = UserRemoteMediator(database) { position ->
                remoteDataSource.searchBy(
                    username,
                    position
                )
            },
            pagingSourceFactory = { database.userDao.searchUserPagingBy(username) }
        ).flow
    }

    override fun getDetailBy(username: String): Flow<UIState<UserEntity>> {
        return localDataSource.getDetailBy(username).map {
            if (it.name.isEmpty()) {
                val uiState = remoteDataSource.getDetailBy(username).mapToUiState {
                    val entity = this.body.toEntity()
                    localDataSource.insertUsers(entity)
                    UIState.Success(entity)
                }
                uiState
            } else
                UIState.Success(it)
        }.onStart {
            emit(UIState.Loading())
        }.flowOn(ioDispatcher)
    }

    override fun getFollowerPaging(username: String): Flow<PagingData<UserEntity>> =
        remoteDataSource.getFollowerPaging(username).flowOn(ioDispatcher)

    override fun getFollowingPaging(username: String): Flow<PagingData<UserEntity>> =
        remoteDataSource.getFollowingPaging(username).flowOn(ioDispatcher)

    override fun getFavorite(): Flow<UIState<List<UserEntity>>> =
        localDataSource.getFavorite().map {
            if (it.isEmpty())
                UIState.Error("There is no favorite user")
            else
                UIState.Success(it)
        }.onStart {
            emit(UIState.Loading())
        }.catch {
            emit(UIState.Error(it.localizedMessage))
        }.flowOn(ioDispatcher)

    override suspend fun setFavorite(userDto: UserEntity): UserEntity {
        return when (userDto.isFavorite) {
            true -> localDataSource.unFavorite(userDto)
            false -> localDataSource.favorite(userDto)
        }
    }

}

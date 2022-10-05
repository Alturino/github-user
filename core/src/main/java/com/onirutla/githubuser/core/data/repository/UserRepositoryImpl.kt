package com.onirutla.githubuser.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onirutla.githubuser.core.data.UIState
import com.onirutla.githubuser.core.data.source.UserRemoteMediator
import com.onirutla.githubuser.core.data.source.local.db.GithubUserDatabase
import com.onirutla.githubuser.core.data.source.remote.mapToUiState
import com.onirutla.githubuser.core.domain.data.User
import com.onirutla.githubuser.core.domain.repository.UserRepository
import com.onirutla.githubuser.core.domain.source.local.LocalDataSource
import com.onirutla.githubuser.core.domain.source.remote.RemoteDataSource
import com.onirutla.githubuser.core.util.Constant.GITHUB_PAGE_SIZE
import com.onirutla.githubuser.core.util.IoDispatcher
import com.onirutla.githubuser.core.util.entitiesToDomains
import com.onirutla.githubuser.core.util.entityToUser
import com.onirutla.githubuser.core.util.responseToUser
import com.onirutla.githubuser.core.util.toEntity
import com.onirutla.githubuser.core.util.toUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ExperimentalPagingApi
internal class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val database: GithubUserDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override fun searchByPaging(username: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = GITHUB_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = UserRemoteMediator(database) { position ->
                remoteDataSource.searchBy(
                    username,
                    position
                )
            },
            pagingSourceFactory = { database.userDao.searchUserPagingBy(username) }
        ).flow.entityToUser()
    }

    override fun getDetailBy(username: String): Flow<UIState<User>> {
        return localDataSource.getDetailBy(username).map {
            val user = it.toUser()
            if (it.name.isEmpty()) {
                val uiState = remoteDataSource.getDetailBy(username).mapToUiState {
                    val entity = this.body.toEntity()
                    localDataSource.insertUsers(entity)
                    UIState.Success(entity.toUser())
                }
                uiState
            } else
                UIState.Success(user)
        }.onStart {
            emit(UIState.Loading())
        }.flowOn(ioDispatcher)
    }

    override fun getFollowerPaging(username: String): Flow<PagingData<User>> =
        remoteDataSource.getFollowerPaging(username)
            .flowOn(ioDispatcher)
            .responseToUser()

    override fun getFollowingPaging(username: String): Flow<PagingData<User>> =
        remoteDataSource.getFollowingPaging(username)
            .flowOn(ioDispatcher)
            .responseToUser()

    override fun getFavorite(): Flow<UIState<List<User>>> =
        localDataSource.getFavorite().map {
            val users = it.entitiesToDomains()
            if (users.isEmpty())
                UIState.Error("There is no favorite user")
            else
                UIState.Success(users)
        }.onStart {
            emit(UIState.Loading())
        }.catch {
            emit(UIState.Error(it.localizedMessage))
        }.flowOn(ioDispatcher)

    override suspend fun setFavorite(userDto: User): User {
        return when (userDto.isFavorite) {
            true -> localDataSource.unFavorite(userDto.toEntity()).toUser()
            false -> localDataSource.favorite(userDto.toEntity()).toUser()
        }
    }

}

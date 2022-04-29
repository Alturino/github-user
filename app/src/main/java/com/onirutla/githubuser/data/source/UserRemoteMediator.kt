package com.onirutla.githubuser.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.onirutla.githubuser.data.source.local.db.GithubUserDatabase
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.data.source.local.entity.UserRemoteKey
import com.onirutla.githubuser.data.source.remote.network.GithubApiService
import com.onirutla.githubuser.data.source.remote.response.toEntity
import okio.IOException
import retrofit2.HttpException

@ExperimentalPagingApi
class UserRemoteMediator(
    private val db: GithubUserDatabase,
    private val apiService: GithubApiService,
    private val query: String,
) : RemoteMediator<Int, UserEntity>() {

    private val userDao = db.userDao
    private val remoteKeyDao = db.userRemoteKeyDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }

            val response = apiService.searchBy(query, currentPage).body()?.items
            val entity = response?.map { it.toEntity() }
            val endOfPaginationReached = entity.isNullOrEmpty()

            val prevPage = if (currentPage == 1) null else currentPage.minus(1)
            val nextPage = if (endOfPaginationReached) null else currentPage.plus(1)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.clearUsers()
                    remoteKeyDao.clearRemoteKeys()
                }

                val keys = entity?.map { userEntity ->
                    UserRemoteKey(
                        userEntity.id,
                        prevPage,
                        nextPage
                    )
                }

                remoteKeyDao.addAllRemoteKeys(keys ?: emptyList())
                userDao.insertUsers(entity ?: emptyList())
            }

            MediatorResult.Success(endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UserEntity>
    ): UserRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                remoteKeyDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UserEntity>
    ): UserRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { userEntity ->
                remoteKeyDao.getRemoteKeys(id = userEntity.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UserEntity>
    ): UserRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKeys(id = id)
            }
        }
    }
}
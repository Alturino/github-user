package com.onirutla.githubuser.core.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.onirutla.githubuser.core.data.source.local.db.GithubUserDatabase
import com.onirutla.githubuser.core.data.source.local.entity.UserEntity
import com.onirutla.githubuser.core.data.source.local.entity.UserRemoteKey
import com.onirutla.githubuser.core.data.source.remote.NetworkResponse
import com.onirutla.githubuser.core.data.source.remote.getData
import com.onirutla.githubuser.core.data.source.remote.response.UserResponse
import com.onirutla.githubuser.core.util.toEntity
import okio.IOException
import retrofit2.HttpException

@ExperimentalPagingApi
internal class UserRemoteMediator(
    private val db: GithubUserDatabase,
    private inline val apiService: suspend (position: Int) -> NetworkResponse<List<UserResponse>>,
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

            val users = apiService(currentPage).getData { body }

            val entity = users?.map { it.toEntity() }
            val endOfPaginationReached = entity?.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage.minus(1)
            val nextPage = if (endOfPaginationReached == true) null else currentPage.plus(1)

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

                keys?.let { remoteKeyDao.addAllRemoteKeys(it) }
                entity?.let { userDao.insertUsers(*it.toTypedArray()) }
            }

            MediatorResult.Success(endOfPaginationReached == true)

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
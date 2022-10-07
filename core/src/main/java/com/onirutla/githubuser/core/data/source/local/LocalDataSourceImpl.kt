package com.onirutla.githubuser.core.data.source.local

import com.onirutla.githubuser.core.data.source.local.dao.UserDao
import com.onirutla.githubuser.core.data.source.local.entity.UserEntity
import com.onirutla.githubuser.core.domain.source.local.LocalDataSource
import com.onirutla.githubuser.core.util.IoDispatcher
import com.onirutla.githubuser.core.util.ensureFlowNotNull
import com.onirutla.githubuser.core.util.ensureFlowOfListNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun searchBy(username: String): Flow<List<UserEntity>> =
        userDao.getUserSearch(username)
            .ensureFlowOfListNotNull(ioDispatcher)

    override fun getFavorite(): Flow<List<UserEntity>> =
        userDao.getFavorites()
            .ensureFlowOfListNotNull(ioDispatcher)


    override fun getDetailBy(username: String): Flow<UserEntity> =
        userDao.getUserDetail(username)
            .ensureFlowNotNull(ioDispatcher)

    override suspend fun insertUsers(vararg users: UserEntity) = userDao.insertUsers(*users)

    override suspend fun favorite(userEntity: UserEntity): UserEntity {
        val favorite = userEntity.copy(isFavorite = true)
        userDao.updateFavorite(favorite)
        return favorite
    }

    override suspend fun unFavorite(userEntity: UserEntity): UserEntity {
        val unFavorite = userEntity.copy(isFavorite = false)
        userDao.updateFavorite(unFavorite)
        return unFavorite
    }
}

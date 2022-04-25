package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.util.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun searchBy(username: String): Flow<List<UserEntity>> =
        userDao.getUserSearch(username)
            .mapNotNull { it }
            .flowOn(ioDispatcher)

    override fun getFavorite(): Flow<List<UserEntity>> =
        userDao.getFavorites()
            .mapNotNull { it }
            .flowOn(ioDispatcher)


    override fun getDetailBy(username: String): Flow<UserEntity> =
        userDao.getUserDetail(username)
            .mapNotNull { it }
            .flowOn(ioDispatcher)

    override suspend fun insertUsers(users: List<UserEntity>) = userDao.insertUsers(users)

    override suspend fun insertUserDetail(userEntity: UserEntity) = userDao.insertUser(userEntity)

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

package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.util.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun getUserSearch(username: String): Flow<FromDb<List<UserEntity>>> =
        userDao.getUserSearch(username).map {
            if (it.isNullOrEmpty())
                FromDb.Empty("You don't have any favorite yet")
            else
                FromDb.Success(it)
        }.flowOn(ioDispatcher)

    override fun getFavorite(): Flow<FromDb<List<UserEntity>>> = userDao.getFavorites().map {
        if (it.isNullOrEmpty())
            FromDb.Empty("You don't have any favorite yet")
        else
            FromDb.Success(it)
    }.flowOn(ioDispatcher)


    override fun getUserDetail(username: String): Flow<FromDb<UserEntity>> =
        userDao.getUserDetail(username).map {
            return@map if (it == null) {
                FromDb.Empty("User not found in database")
            } else {
                FromDb.Success(it)
            }
        }.flowOn(ioDispatcher)

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

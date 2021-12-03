package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val userDao: UserDao) {

    fun getUserSearch(username: String): Flow<FromDb<List<UserEntity>>> =
        userDao.getUserSearch(username).map {
            if (it.isNullOrEmpty())
                FromDb.Empty("You don't have any favorite yet")
            else
                FromDb.Success(it)
        }

    fun getFavorite(): Flow<FromDb<List<UserEntity>>> = userDao.getFavorites().map {
        if (it.isNullOrEmpty())
            FromDb.Empty("You don't have any favorite yet")
        else
            FromDb.Success(it)
    }


    fun getUserDetail(username: String): Flow<FromDb<UserEntity>> =
        userDao.getUserDetail(username).map {
            return@map if (it == null) {
                FromDb.Empty("User not found in database")
            } else {
                FromDb.Success(it)
            }
        }

    suspend fun insertUsers(users: List<UserEntity>) = userDao.insertUsers(users)

    suspend fun insertUserDetail(userEntity: UserEntity) = userDao.insertUser(userEntity)

    suspend fun favorite(userEntity: UserEntity): UserEntity {
        userEntity.isFavorite = true
        userDao.updateFavorite(userEntity)
        return userEntity
    }

    suspend fun unFavorite(userEntity: UserEntity): UserEntity {
        userEntity.isFavorite = false
        userDao.updateFavorite(userEntity)
        return userEntity
    }
}
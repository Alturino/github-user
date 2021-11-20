package com.onirutla.githubuser.data.source.local

import com.onirutla.githubuser.data.source.local.dao.UserDao
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val userDao: UserDao) {

    fun getFavorite(): Flow<List<UserEntity>> = userDao.getFavorites()

    suspend fun insertUsers(users: List<UserEntity>) = userDao.insertUsers(users)

    suspend fun getUserSearch(username: String) = userDao.getUserSearch(username)

    suspend fun insertUserDetail(userEntity: UserEntity) = userDao.insertUser(userEntity)

    suspend fun getUserDetail(username: String) = userDao.getUserDetail(username)

    suspend fun favorite(userEntity: UserEntity) {
        userEntity.isFavorite = true
        userDao.updateFavorite(userEntity)
    }

    suspend fun unFavorite(userEntity: UserEntity) {
        userEntity.isFavorite = false
        userDao.updateFavorite(userEntity)
    }
}
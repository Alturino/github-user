package com.onirutla.githubuser.data.local

import android.util.Log
import com.onirutla.githubuser.data.local.dao.UserDao
import com.onirutla.githubuser.data.local.entity.UserEntity
import com.onirutla.githubuser.util.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun findUserByUsername(username: String): Flow<List<UserEntity>> =
        userDao.findUserByUsername(username)
            .catch {
                Log.d("findUser localsource", "$it")
            }.flowOn(dispatcher)


    override fun getFavoriteUsers(): Flow<List<UserEntity>> = userDao.getFavorites()
        .catch {
            Log.d("favorite localsource", "$it")
        }.flowOn(dispatcher)

    override fun getUserDetail(username: String): Flow<UserEntity?> =
        userDao.getUserDetail(username).filterNotNull()
            .catch { Log.d("userDetail localsource", "$it") }.flowOn(dispatcher)

    override suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    override suspend fun insertUsers(users: List<UserEntity>) = userDao.insertUsers(users)

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

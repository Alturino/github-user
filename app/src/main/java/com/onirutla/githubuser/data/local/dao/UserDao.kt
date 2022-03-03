package com.onirutla.githubuser.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.onirutla.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Insert(onConflict = ABORT)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateFavorite(user: UserEntity)

    @Query(value = "SELECT * FROM USER WHERE username = :username")
    fun getUserDetail(username: String): Flow<UserEntity?>

    @Query(value = "SELECT * FROM USER WHERE is_favorite = 1")
    fun getFavorites(): Flow<List<UserEntity>>

    @Query(value = "SELECT * FROM USER WHERE username like :username || '%'")
    fun findUserByUsername(username: String): Flow<List<UserEntity>>

}

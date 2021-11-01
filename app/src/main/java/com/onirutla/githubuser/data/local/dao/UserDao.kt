package com.onirutla.githubuser.data.local.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.onirutla.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertUserSearch(users: List<UserEntity>)

    @Update
    suspend fun updateFavorite(userEntity: UserEntity)

    @Query(value = "SELECT * FROM USER where is_favorite = 1")
    suspend fun getFavorites(): List<UserEntity>

    @Query(value = "SELECT * FROM USER WHERE username like '%' || :username || '%'")
    suspend fun getUserSearch(username: String): List<UserEntity>

}
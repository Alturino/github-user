package com.onirutla.githubuser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateFavorite(userEntity: UserEntity)

    @Query(value = "SELECT * FROM USER where username = :username")
    fun getUserDetail(username: String): Flow<UserEntity?>

    @Query(value = "SELECT * FROM USER where is_favorite = 1")
    fun getFavorites(): Flow<List<UserEntity>>

    @Query(value = "SELECT * FROM USER WHERE username like :username || '%'")
    fun getUserSearch(username: String): Flow<List<UserEntity>>

}

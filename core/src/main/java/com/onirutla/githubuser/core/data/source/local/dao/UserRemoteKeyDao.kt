package com.onirutla.githubuser.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onirutla.githubuser.core.data.source.local.entity.UserRemoteKey

@Dao
internal interface UserRemoteKeyDao {

    @Query("SELECT * FROM remote_key WHERE id =:id")
    suspend fun getRemoteKeys(id: Int): UserRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(userRemoteKeys: List<UserRemoteKey>)

    @Query("DELETE FROM remote_key")
    suspend fun clearRemoteKeys()

}
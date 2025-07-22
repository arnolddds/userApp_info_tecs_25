package com.sobolev.userapp_infotecs_25.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sobolev.userapp_infotecs_25.data.local.models.UserDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserDbModel>>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUser(userId: Int): UserDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserDbModel>)

    @Query("DELETE FROM users")
    suspend fun clearAll()
}


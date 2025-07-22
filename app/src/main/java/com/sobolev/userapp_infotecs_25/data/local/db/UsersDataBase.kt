package com.sobolev.userapp_infotecs_25.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobolev.userapp_infotecs_25.data.local.models.UserDbModel

@Database(
    entities = [UserDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class UsersDataBase : RoomDatabase() {

    abstract fun usersDao(): UsersDao

}
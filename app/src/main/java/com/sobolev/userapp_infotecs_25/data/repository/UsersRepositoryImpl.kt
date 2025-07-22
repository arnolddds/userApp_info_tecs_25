package com.sobolev.userapp_infotecs_25.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.sobolev.userapp_infotecs_25.data.local.db.UsersDao
import com.sobolev.userapp_infotecs_25.data.local.models.UserDbModel
import com.sobolev.userapp_infotecs_25.data.mapper.toDbModel
import com.sobolev.userapp_infotecs_25.data.mapper.toDomain
import com.sobolev.userapp_infotecs_25.data.network.ApiService
import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.domain.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class UsersRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val usersDao: UsersDao
) : UsersRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return usersDao.getAllUsers()
            .map { list: List<UserDbModel> -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getUser(userId: Int): User {
        return usersDao.getUser(userId).toDomain()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun refreshUsers() {
        val remoteUsers = apiService.getAllUsers(50).results
        val dbModels = remoteUsers.map { it.toDbModel() }
        usersDao.clearAll()
        usersDao.insertAll(dbModels)
    }
}


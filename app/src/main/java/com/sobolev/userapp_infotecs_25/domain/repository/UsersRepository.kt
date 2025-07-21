package com.sobolev.userapp_infotecs_25.domain.repository

import com.sobolev.userapp_infotecs_25.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun getAllUsers(): Flow<List<User>>

    suspend fun getUser(userId: Int): User

    suspend fun refreshUsers()

}
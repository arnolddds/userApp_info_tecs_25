package com.sobolev.userapp_infotecs_25.domain.usecases

import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(): Flow<List<User>> = repository.getAllUsers()
}

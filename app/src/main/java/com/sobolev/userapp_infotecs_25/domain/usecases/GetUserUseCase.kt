package com.sobolev.userapp_infotecs_25.domain.usecases

import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.domain.repository.UsersRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(userId: Int): User {
        return repository.getUser(userId)
    }
}
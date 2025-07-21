package com.sobolev.userapp_infotecs_25.domain.usecases



import com.sobolev.userapp_infotecs_25.domain.repository.UsersRepository
import javax.inject.Inject

class RefreshUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke() = repository.refreshUsers()
}
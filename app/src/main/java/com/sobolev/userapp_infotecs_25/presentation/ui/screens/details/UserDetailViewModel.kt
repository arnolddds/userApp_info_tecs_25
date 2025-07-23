package com.sobolev.userapp_infotecs_25.presentation.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.domain.usecases.GetUserUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@HiltViewModel(assistedFactory = UserDetailViewModel.Factory::class)
class UserDetailViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: Int,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UserDetailState>(UserDetailState.Initial)
    val state = _state.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: Int
        ): UserDetailViewModel
    }

    init {
        viewModelScope.launch {
            try {
                val user = getUserUseCase(userId)
                _state.update {
                    UserDetailState.Checking(user)
                }
            } catch (e: Exception) {
                _state.update {
                    UserDetailState.Error(e.toUserFriendlyMessage())
                }
            }
        }
    }


    fun processCommand(command: UserDetailCommand) {
        when (command) {
            UserDetailCommand.Back -> {
                _state.update {
                    UserDetailState.Finished
                }
            }
        }
    }

    private fun Throwable.toUserFriendlyMessage(): String = when (this) {
        is UnknownHostException -> "No internet"
        is TimeoutException, is SocketTimeoutException -> "Server is not responding"
        else -> "There was an error getting the user"
    }



}

sealed interface UserDetailCommand {

    data object Back : UserDetailCommand
}

sealed interface UserDetailState {
    data object Initial : UserDetailState
    data class Checking(val user: User) : UserDetailState
    data class Error(val message: String) : UserDetailState
    data object Finished : UserDetailState
}

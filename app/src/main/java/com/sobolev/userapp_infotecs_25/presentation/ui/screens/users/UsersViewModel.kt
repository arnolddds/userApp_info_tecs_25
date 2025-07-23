package com.sobolev.userapp_infotecs_25.presentation.ui.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.domain.usecases.GetAllUsersUseCase
import com.sobolev.userapp_infotecs_25.domain.usecases.RefreshUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val refreshUsersUseCase: RefreshUsersUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(UsersScreenState(isLoading = true))
    val screenState = _screenState.asStateFlow()

    init {
        observeUsers()
    }


    private fun observeUsers() {
        viewModelScope.launch {
            getAllUsersUseCase()
                .onStart {
                    _screenState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    val friendlyMessage = when {
                        e is UnknownHostException -> "No internet connection"
                        else -> "Error loading users"
                    }

                    _screenState.update {
                        it.copy(error = friendlyMessage, isLoading = false)
                    }
                }

                .collect { users ->
                    val shouldRefresh = users.isEmpty() && _screenState.value.isRefreshing.not()

                    val errorMessage = if (users.isEmpty() && !_screenState.value.isRefreshing) {
                        "The user list is empty. Check your network connection."
                    } else null

                    _screenState.update {
                        it.copy(
                            allUsers = users,
                            isLoading = false,
                            error = errorMessage
                        )
                    }

                    if (shouldRefresh) {
                        refresh()
                    }
                }

        }
    }

    fun refresh() {
        viewModelScope.launch {
            _screenState.update { it.copy(isRefreshing = true, error = null) }

            try {
                refreshUsersUseCase()
            } catch (e: Exception) {
                val friendlyMessage = when {
                    e is UnknownHostException -> "No internet connection"
                    else -> "Error loading users"
                }

                _screenState.update {
                    it.copy(error = friendlyMessage)
                }
            } finally {
                _screenState.update { it.copy(isRefreshing = false) }
            }

        }
    }


}


data class UsersScreenState(
    val allUsers: List<User> = listOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
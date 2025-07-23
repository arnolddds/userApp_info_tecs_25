package com.sobolev.userapp_infotecs_25.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sobolev.userapp_infotecs_25.presentation.ui.screens.details.UserDetailsScreen
import com.sobolev.userapp_infotecs_25.presentation.ui.screens.users.UsersScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Users.route
    ) {
        composable(Screen.Users.route) {
            UsersScreen(
                onUserClick = {
                    navController.navigate(Screen.UserInfo.createRoute(it.id))
                },
            )
        }

        composable(Screen.UserInfo.route) {
            val userId = Screen.UserInfo.getUserId(it.arguments)
            UserDetailsScreen(
                userId = userId,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }

}

sealed class Screen(val route: String) {

    data object Users : Screen("users")

    data object UserInfo : Screen("user_info/{user_id}") {
        fun createRoute(userId: Int) = "user_info/$userId"

        fun getUserId(arguments: Bundle?): Int {
            return arguments?.getString("user_id")?.toIntOrNull() ?: 0
        }
    }
}
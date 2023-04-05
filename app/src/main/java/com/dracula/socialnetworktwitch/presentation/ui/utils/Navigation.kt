package com.dracula.socialnetworktwitch.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dracula.socialnetworktwitch.presentation.ui.chat.ChatScreen
import com.dracula.socialnetworktwitch.presentation.ui.login.LoginScreen
import com.dracula.socialnetworktwitch.presentation.ui.main_feed.MainFeedScreen
import com.dracula.socialnetworktwitch.presentation.ui.register.RegisterScreen
import com.dracula.socialnetworktwitch.presentation.ui.splash.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(
            route = Screens.LoginScreen.route
        ) {
            LoginScreen(navController = navController)
        }
        composable(route = Screens.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        composable(route = Screens.MainFeedScreen.route) {
            MainFeedScreen(navController = navController)
        }
        composable(route = Screens.ChatScreen.route) {
            ChatScreen(navController = navController)
        }

        composable(route = Screens.NotificationsScreen.route) {

        }
        composable(route = Screens.ProfileScreen.route) {

        }
    }
}
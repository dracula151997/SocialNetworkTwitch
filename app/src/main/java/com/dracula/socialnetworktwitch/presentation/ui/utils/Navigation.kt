package com.dracula.socialnetworktwitch.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dracula.socialnetworktwitch.presentation.ui.login.LoginScreen
import com.dracula.socialnetworktwitch.presentation.ui.splash.SplashScreen

@Composable
fun Navigation(
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(
            route = Screen.LoginScreen.route
        ) {
            LoginScreen(navController = navController)
        }
    }
}
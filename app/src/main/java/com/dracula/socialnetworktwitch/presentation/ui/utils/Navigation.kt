package com.dracula.socialnetworktwitch.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dracula.socialnetworktwitch.presentation.ui.login.LoginScreen
import com.dracula.socialnetworktwitch.presentation.ui.mainfeed.MainFeedScreen
import com.dracula.socialnetworktwitch.presentation.ui.register.RegisterScreen
import com.dracula.socialnetworktwitch.presentation.ui.splash.SplashScreen

@Composable
fun Navigation(
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(
            route = Screens.LoginScreen.route
        ) {
            LoginScreen(navController = navController)
        }
        composable(route = Screens.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }

        composable(route = Screens.MainFeedScreen.route){
            MainFeedScreen(navController = navController)
        }
    }
}
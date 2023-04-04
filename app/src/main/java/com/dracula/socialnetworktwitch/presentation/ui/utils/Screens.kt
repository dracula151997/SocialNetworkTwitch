package com.dracula.socialnetworktwitch.presentation.ui.utils

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object LoginScreen : Screens("login_screen")

    object RegisterScreen : Screens("register_screen")

    object MainFeedScreen: Screens("feeds_screen")
}
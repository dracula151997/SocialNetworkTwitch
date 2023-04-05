package com.dracula.socialnetworktwitch.presentation.ui.utils

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object LoginScreen : Screens("login_screen")

    object RegisterScreen : Screens("register_screen")

    object MainFeedScreen: Screens("feeds_screen")

    object ChatScreen: Screens("chat_screen")

    object NotificationsScreen: Screens("notifications_screen")

    object ProfileScreen: Screens("profile_screen")

    object CreatePostScreen: Screens("create_profile_screen")
}
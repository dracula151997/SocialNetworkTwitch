package com.dracula.socialnetworktwitch.core.presentation.utils

import androidx.annotation.StringRes
import com.dracula.socialnetworktwitch.R

sealed class Screens(val route: String, @StringRes val title: Int? = null) {
    object SplashScreen : Screens("splash_screen")
    object LoginScreen : Screens("login_screen")

    object RegisterScreen : Screens("register_screen")

    object MainFeedScreen : Screens("feeds_screen", title = R.string.your_feed)

    object MessagesScreen : Screens("chat_screen")

    object NotificationsScreen : Screens("notifications_screen")

    object ProfileScreen : Screens("profile_screen")

    object CreatePostScreen : Screens("create_profile_screen")

    object PostDetailsScreen : Screens("post_details_screen", title = R.string.your_feed)
    object EditProfileScreen : Screens("edit_profile_screen")
    object SearchScreen : Screens("search_screen")
    object PersonListScreen : Screens("person_list_screen")
}
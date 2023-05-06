package com.dracula.socialnetworktwitch.core.presentation.utils

import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.Constants

sealed class Screens(val route: String, @StringRes val title: Int? = null) {
    object SplashScreen : Screens("splash_screen")
    object LoginScreen : Screens("login_screen")

    object RegisterScreen : Screens("register_screen")

    object MainFeedScreen : Screens("feeds_screen", title = R.string.your_feed)

    object MessagesScreen : Screens("chat_screen")

    object NotificationsScreen : Screens("notifications_screen")

    object ProfileScreen :
        Screens("profile_screen?userId={${Constants.NavArguments.NAV_USER_ID}}") {
        fun createRoute(userId: String?): String {
            return "profile_screen?userId=${userId}"
        }

        val navArgs
            get() = listOf(navArgument(Constants.NavArguments.NAV_USER_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
    }

    object CreatePostScreen : Screens("create_profile_screen")

    object PostDetailsScreen :
        Screens("post_details_screen?postId={${Constants.NavArguments.NAV_POST_ID}}") {
        fun createRoute(postId: String?): String {
            return "post_details_screen?postId=${postId}"
        }

        val navArgs
            get() = listOf(navArgument(Constants.NavArguments.NAV_POST_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
    }

    object EditProfileScreen :
        Screens("edit_profile_screen?userId={${Constants.NavArguments.NAV_USER_ID}}") {
        fun createRoute(userId: String?): String {
            return "edit_profile_screen?${Constants.NavArguments.NAV_USER_ID}=${userId}"
        }

        val navArgs
            get() = listOf(navArgument(Constants.NavArguments.NAV_USER_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
    }

    object SearchScreen : Screens("search_screen")
    object PersonListScreen : Screens("person_list_screen")
}
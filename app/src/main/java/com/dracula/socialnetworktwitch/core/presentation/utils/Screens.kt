package com.dracula.socialnetworktwitch.core.presentation.utils

import android.content.Intent
import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.Constants

sealed class Screens(val route: String, @StringRes val title: Int? = null) {
    object SplashScreen : Screens("splash_screen")
    object LoginScreen : Screens("login_screen")

    object RegisterScreen : Screens("register_screen")

    object MainFeedScreen : Screens("feeds_screen", title = R.string.your_feed)

    object ChatListScreen : Screens("chats_screen")
    object MessageScreen : Screens("messages_screen")

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
        Screens("post_details_screen?postId={post_id}&showKeyboard={show_keyboard}") {
        fun createRoute(postId: String?, showKeyboard: Boolean = false): String {
            return "post_details_screen?postId=${postId}&showKeyboard=${showKeyboard}"
        }

        val deepLink = listOf(
            navDeepLink {
                uriPattern = "${DEEP_LINK_URL}/post/details/{post_id}"
                action = Intent.ACTION_VIEW
            }
        )

        val navArgs
            get() = listOf(
                navArgument(Constants.NavArguments.NAV_POST_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(Constants.NavArguments.NAV_SHOW_KEYBOARD) {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
    }

    object EditProfileScreen :
        Screens("edit_profile_screen?userId={${Constants.NavArguments.NAV_USER_ID}}") {
        fun createRoute(userId: String?): String {
            return "edit_profile_screen?userId=${userId}"
        }

        val navArgs
            get() = listOf(navArgument(Constants.NavArguments.NAV_USER_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
    }

    object SearchScreen : Screens("search_screen")
    object PersonListScreen : Screens("person_list_screen?parentId={parent_id}") {
        fun createRoute(parentId: String?): String {
            return "person_list_screen?parentId=${parentId}"
        }

        val navArgs
            get() = listOf(navArgument(Constants.NavArguments.NAV_PARENT_ID) {
                type = NavType.StringType
            })
    }

    companion object {
        const val DEEP_LINK_URL = "social://dracula.com"
    }
}
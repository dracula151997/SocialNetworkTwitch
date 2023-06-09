package com.dracula.socialnetworktwitch.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dracula.socialnetworktwitch.feature_post.domain.Post
import com.dracula.socialnetworktwitch.feature_activity.presentation.ActivityScreen
import com.dracula.socialnetworktwitch.presentation.ui.chat.ChatScreen
import com.dracula.socialnetworktwitch.feature_post.presentation.create_post.CreatePostScreen
import com.dracula.socialnetworktwitch.feature_profile.edit_profile.EditProfileScreen
import com.dracula.socialnetworktwitch.feature_auth.presentation.login.LoginScreen
import com.dracula.socialnetworktwitch.presentation.ui.main_feed.MainFeedScreen
import com.dracula.socialnetworktwitch.feature_post.presentation.post_details.PostDetailsScreen
import com.dracula.socialnetworktwitch.presentation.ui.profile.ProfileScreen
import com.dracula.socialnetworktwitch.feature_auth.presentation.register.RegisterScreen
import com.dracula.socialnetworktwitch.feature_search.presentation.SearchScreen
import com.dracula.socialnetworktwitch.feature_splash.presentation.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route,
        modifier = modifier
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
        composable(route = Screens.MessagesScreen.route) {
            ChatScreen(navController = navController)
        }

        composable(route = Screens.NotificationsScreen.route) {
            ActivityScreen(navController = navController)
        }
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = Screens.CreatePostScreen.route) {
            CreatePostScreen(navController = navController)
        }

        composable(route = Screens.PostDetailsScreen.route) {
            PostDetailsScreen(navController = navController, post = Post.dummy())
        }

        composable(route = Screens.EditProfileScreen.route) {
            EditProfileScreen(navController = navController)
        }

        composable(route = Screens.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
    }
}
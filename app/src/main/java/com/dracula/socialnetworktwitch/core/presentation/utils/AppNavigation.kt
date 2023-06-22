package com.dracula.socialnetworktwitch.core.presentation.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_activity.presentation.ActivityRoute
import com.dracula.socialnetworktwitch.feature_auth.presentation.login.LoginRoute
import com.dracula.socialnetworktwitch.feature_auth.presentation.register.RegisterRoute
import com.dracula.socialnetworktwitch.feature_chat.presentation.chat.ChatRoute
import com.dracula.socialnetworktwitch.feature_chat.presentation.message.MessageRoute
import com.dracula.socialnetworktwitch.feature_main_feed.MainFeedRoute
import com.dracula.socialnetworktwitch.feature_post.presentation.create_post.CreatePostRoute
import com.dracula.socialnetworktwitch.feature_post.presentation.person_list.PersonListRoute
import com.dracula.socialnetworktwitch.feature_post.presentation.post_details.PostDetailsRoute
import com.dracula.socialnetworktwitch.feature_profile.edit_profile.EditProfileRoute
import com.dracula.socialnetworktwitch.feature_profile.profile.ProfileRoute
import com.dracula.socialnetworktwitch.feature_search.presentation.SearchRoute
import com.dracula.socialnetworktwitch.feature_splash.presentation.SplashRoute
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route,
        modifier = modifier
    ) {
        composable(route = Screens.SplashScreen.route) {
            SplashRoute(
                onNavigate = {
                    navController.navigate(it) {
                        popUpTo(Screens.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = Screens.LoginScreen.route
        ) {
            LoginRoute(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                showSnackbar = { message ->
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = message)
                    }
                }
            )
        }
        composable(route = Screens.RegisterScreen.route) {
            RegisterRoute(
                onNavigate = navController::navigate,
                onNavUp = navController::navigateUp,
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                }
            )
        }

        composable(route = Screens.MainFeedScreen.route) {
            MainFeedRoute(
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding()),
                onNavigate = { navController.navigate(it) },
                onNavUp = { navController.navigateUp() },
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                }
            )
        }
        composable(route = Screens.ChatListScreen.route) {
            ChatRoute(
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding()),
                onNavigate = navController::navigate,
                onNavUp = navController::navigateUp,
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                }
            )

        }
        composable(
            route = Screens.MessageScreen.route,
            arguments = Screens.MessageScreen.navArgs
        ) {
            val remoteUserProfilePic =
                it.arguments?.getString(Constants.NavArguments.NAV_REMOTE_USER_PROFILE_PICTURE)
                    .orEmpty()
            val remoteUserId =
                it.arguments?.getString(Constants.NavArguments.NAV_REMOTE_USER_ID).orEmpty()
            val remoteUserName =
                it.arguments?.getString(Constants.NavArguments.NAV_REMOTE_USER_NAME).orEmpty()
            MessageRoute(
                encodedRemoteUserProfilePic = remoteUserProfilePic,
                onNavUp = navController::navigateUp,
                remoteUserId = remoteUserId,
                remoteUsername = remoteUserName
            )
        }

        composable(route = Screens.NotificationsScreen.route) {
            ActivityRoute(
                onNavigate = navController::navigate,
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                }
            )
        }
        composable(
            route = Screens.ProfileScreen.route,
            arguments = Screens.ProfileScreen.navArgs,
        ) {
            val userId = it.arguments?.getString(Constants.NavArguments.NAV_USER_ID)
            ProfileRoute(
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding()),
                userId = userId,
                onNavigate = navController::navigate,
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                },
                navigateToLogin = {
                    navController.navigate(Screens.LoginScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screens.CreatePostScreen.route) {
            CreatePostRoute(
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                },
                onNavUp = navController::navigateUp
            )
        }

        composable(
            route = Screens.PostDetailsScreen.route,
            arguments = Screens.PostDetailsScreen.navArgs,
            deepLinks = Screens.PostDetailsScreen.deepLink
        ) {
            val showKeyboard =
                it.arguments?.getBoolean(Constants.NavArguments.NAV_SHOW_KEYBOARD) ?: false
            PostDetailsRoute(
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                },
                onNavigate = navController::navigate,
                onNavUp = navController::navigateUp,
                showKeyboard = { showKeyboard }
            )
        }

        composable(
            route = Screens.EditProfileScreen.route,
            arguments = Screens.EditProfileScreen.navArgs
        ) {
            val userId = it.arguments?.getString(Constants.NavArguments.NAV_USER_ID)
            EditProfileRoute(
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                },
                onNavUp = navController::navigateUp,
            )
        }

        composable(route = Screens.SearchScreen.route) {
            SearchRoute(
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                },
                onNavUp = navController::navigateUp,
                onNavigate = navController::navigate
            )
        }
        composable(
            route = Screens.PersonListScreen.route,
            arguments = Screens.PersonListScreen.navArgs
        ) {
            val parentId = it.arguments?.getString(Constants.NavArguments.NAV_PARENT_ID)
            requireNotNull(parentId) {
                "Nav arguments: ${Constants.NavArguments.NAV_PARENT_ID} is null"
            }
            PersonListRoute(
                showSnackbar = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                },
                onNavUp = navController::navigateUp,
                onNavigate = navController::navigate
            )
        }
    }
}
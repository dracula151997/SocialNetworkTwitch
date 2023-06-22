package com.dracula.socialnetworktwitch.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dracula.socialnetworktwitch.core.presentation.components.StandardScaffold
import com.dracula.socialnetworktwitch.core.presentation.theme.SocialNetworkTwitchTheme
import com.dracula.socialnetworktwitch.core.presentation.utils.AppNavigation
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialNetworkTwitchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val scaffoldState = rememberScaffoldState()
                    StandardScaffold(
                        state = scaffoldState,
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        showBottomBar = showBottomBar(navBackStackEntry),
                        onFabClicked = {
                            navController.navigate(Screens.CreatePostScreen.route)
                        },
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            AppNavigation(
                                navController = navController,
                                scaffoldState = scaffoldState,
                                contentPadding = innerPadding

                            )
                        }
                    }
                }
            }
        }
    }
}

private fun showBottomBar(backStackEntry: NavBackStackEntry?): Boolean {
    val route = backStackEntry?.destination?.route.orEmpty()
    val isOwnProfile =
        (route == Screens.ProfileScreen.route) && (backStackEntry?.arguments?.getString(
            Constants.NavArguments.NAV_USER_ID
        ) == null)
    return route in listOf(
        Screens.MainFeedScreen.route,
        Screens.NotificationsScreen.route,
        Screens.ChatListScreen.route
    ) || isOwnProfile
}

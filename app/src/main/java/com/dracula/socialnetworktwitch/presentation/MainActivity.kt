package com.dracula.socialnetworktwitch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.presentation.components.StandardScaffold
import com.dracula.socialnetworktwitch.presentation.ui.theme.SocialNetworkTwitchTheme
import com.dracula.socialnetworktwitch.presentation.ui.utils.Navigation
import com.dracula.socialnetworktwitch.presentation.ui.utils.Screens
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
                    val route = navBackStackEntry?.destination?.route.orEmpty()
                    StandardScaffold(navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        showBottomBar = showBottomBar(route = route),
                        onFabClicked = {
                            navController.navigate(Screens.CreatePostScreen.route)
                        }) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Navigation(
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun showBottomBar(route: String): Boolean {
    return route in listOf(
        Screens.ProfileScreen.route,
        Screens.MainFeedScreen.route,
        Screens.NotificationsScreen.route,
        Screens.MessagesScreen.route
    )
}

private fun showToolbar(route: String): Boolean {
    return route in listOf(
        Screens.MainFeedScreen.route
    )
}

private fun showBackArrow(route: String): Boolean {
    return route in listOf(
        Screens.PostDetailsScreen.route,
        Screens.MessagesScreen.route,
        Screens.EditProfileScreen.route,
        Screens.SearchScreen.route,
        Screens.CreatePostScreen.route,
        Screens.PersonListScreen.route
    )
}

@Composable
private fun getScreenTitle(route: String): String {
    return when (route) {
        Screens.MainFeedScreen.route, Screens.PostDetailsScreen.route -> stringResource(id = R.string.your_feed)
        else -> ""
    }
}
package com.dracula.socialnetworktwitch.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dracula.socialnetworktwitch.core.presentation.components.StandardScaffold
import com.dracula.socialnetworktwitch.core.presentation.theme.SocialNetworkTwitchTheme
import com.dracula.socialnetworktwitch.core.presentation.utils.Navigation
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.UserStateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()
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
                    val scaffoldState = rememberScaffoldState()
                    StandardScaffold(
                        state = scaffoldState,
                        navController = navController,
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
                                scaffoldState = scaffoldState
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

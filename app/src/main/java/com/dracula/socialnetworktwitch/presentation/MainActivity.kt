package com.dracula.socialnetworktwitch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    StandardScaffold(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        showBottomBar = navBackStackEntry?.destination?.route in listOf(
                            Screens.ProfileScreen.route,
                            Screens.MainFeedScreen.route,
                            Screens.NotificationsScreen.route,
                            Screens.ChatScreen.route
                        ),
                        onFabClicked = {
                            navController.navigate(Screens.CreatePostScreen.route)
                        }
                    ) {
                        Navigation(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SocialNetworkTwitchTheme {
        Greeting("Android")
    }
}
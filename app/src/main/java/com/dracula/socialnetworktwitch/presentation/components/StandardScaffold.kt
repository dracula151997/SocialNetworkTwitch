package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.domain.model.BottomNavItem
import com.dracula.socialnetworktwitch.presentation.ui.Semantics
import com.dracula.socialnetworktwitch.presentation.ui.utils.Screens


@Composable
fun StandardScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            route = Screens.MainFeedScreen.route,
            icon = Icons.Outlined.Home,
            contentDescription = Semantics.ContentDescriptions.HOME,
            alertCount = 5
        ),
        BottomNavItem(
            route = Screens.ChatScreen.route,
            icon = Icons.Outlined.Chat,
            contentDescription = Semantics.ContentDescriptions.CHAT,
        ),
        BottomNavItem(
            route = Screens.NotificationsScreen.route,
            icon = Icons.Outlined.Notifications,
            contentDescription = Semantics.ContentDescriptions.ACTIVITY,
        ),
        BottomNavItem(
            route = Screens.ProfileScreen.route,
            icon = Icons.Outlined.Person,
            contentDescription = Semantics.ContentDescriptions.PROFILE,
        )
    ),
    viewModel: StandardScaffoldViewModel = hiltViewModel(),
    showBottomBar: Boolean = true,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar)
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.surface,
                    cutoutShape = CircleShape,

                    ) {
                    BottomNavigation(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for ((index, navItem) in bottomNavItems.withIndex()) {
                            StandardBottomNavItem(
                                icon = navItem.icon,
                                selected = navItem.route == navController.currentDestination?.route,
                                contentDescription = navItem.contentDescription,
                                alertCount = navItem.alertCount,

                                ) {
                                navController.navigate(navItem.route)
                            }
                        }
                    }
                }
        }
    ) { paddingValues ->
        content()
    }

}
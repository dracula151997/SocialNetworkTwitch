package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            route = Screens.MessagesScreen.route,
            icon = Icons.Outlined.Chat,
            contentDescription = Semantics.ContentDescriptions.CHAT,
        ),
        BottomNavItem(
            route = ""
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
    showBottomBar: Boolean = true,
    onFabClicked: () -> Unit = {},
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
                        for (navItem in bottomNavItems) {
                            StandardBottomNavItem(
                                icon = navItem.icon,
                                selected = isNavItemSelected(
                                    currentRoute = navController.currentDestination?.route.orEmpty(),
                                    navRoute = navItem.route
                                ),
                                contentDescription = navItem.contentDescription,
                                alertCount = navItem.alertCount,
                                enabled = navItem.enabled
                            ) {
                                navController.navigate(navItem.route) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
        },
        floatingActionButton = {
            if (showBottomBar)
                FloatingActionButton(
                    onClick = onFabClicked,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = Semantics.ContentDescriptions.MAKE_POST
                    )
                }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        content()
    }

}

fun shouldNavigate(currentRoute: String, navRoute: String): Boolean {
    return currentRoute != navRoute
}

private fun isNavItemSelected(currentRoute: String, navRoute: String) = currentRoute == navRoute

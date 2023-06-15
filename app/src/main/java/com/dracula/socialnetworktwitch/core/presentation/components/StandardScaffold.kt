package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.core.domain.model.BottomNavItem
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens


@Composable
fun StandardScaffold(
    navController: NavController,
    state: ScaffoldState,
    modifier: Modifier = Modifier,
    bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            route = Screens.MainFeedScreen.route,
            icon = Icons.Outlined.Home,
            contentDescription = Semantics.ContentDescriptions.HOME,
        ),
        BottomNavItem(
            route = Screens.ChatListScreen.route,
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
    content: @Composable (contentPadding: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        scaffoldState = state,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
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
                                    popUpTo(Screens.MainFeedScreen.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = scaleIn(),
                exit = scaleOut()
            )
            {
                FloatingActionButton(
                    onClick = onFabClicked,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = Semantics.ContentDescriptions.MAKE_POST
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        content(paddingValues)
    }

}

private fun isNavItemSelected(currentRoute: String, navRoute: String) = currentRoute == navRoute

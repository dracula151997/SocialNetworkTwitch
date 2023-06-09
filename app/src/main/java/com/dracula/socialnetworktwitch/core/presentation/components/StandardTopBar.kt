package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily

@Composable
fun StandardTopBar(
    title: String?,
    navController: NavController,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    showBackButton: Boolean = false,
    navActions: @Composable (RowScope.() -> Unit) = {},
) {

    TopAppBar(
        title = {
            Text(
                text = title.orEmpty(),
                style = MaterialTheme.typography.h6.copy(
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
        },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
        navigationIcon = if (showBackButton) {
            val backIcon: @Composable () -> Unit = {
                BackIcon(onBackClicked = { navController.navigateUp() })
            }
            backIcon
        } else navigationIcon,
        actions = navActions
    )
}

@Composable
fun StandardTopBar(
    title: @Composable () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    showBackButton: Boolean = false,
    navActions: @Composable() (RowScope.() -> Unit) = {},
) {

    TopAppBar(
        title = title,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
        navigationIcon = if (showBackButton) {
            val backIcon: @Composable () -> Unit = {
                BackIcon(
                    onBackClicked = { navController.navigateUp() },
                )
            }
            backIcon
        } else navigationIcon,
        actions = navActions
    )
}
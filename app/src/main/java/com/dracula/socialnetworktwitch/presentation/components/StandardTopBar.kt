package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dracula.socialnetworktwitch.presentation.ui.theme.TextWhite

@Composable
fun StandardTopBar(
    title: String?,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    navigationIcon: (@Composable () -> Unit)? = null,
    onBackClicked: () -> Unit = {},
    navActions: @Composable() (RowScope.() -> Unit) = {},
    ) {
    TopAppBar(
        title = {
            Text(text = title.orEmpty(), fontWeight = FontWeight.Bold)
        },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        navigationIcon = {
            if (showBackButton) {
                BackIcon(
                    onBackClicked = onBackClicked,
                    tint = MaterialTheme.colors.onBackground
                )
            } else {
                navigationIcon?.invoke()
            }
        },
        actions = navActions
    )
}
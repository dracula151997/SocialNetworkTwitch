package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StandardTopBar(
    title: String?,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable() (() -> Unit)? = null,
    navActions: @Composable() (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        title = {
            Text(text = title.orEmpty(), fontWeight = FontWeight.Bold)
        },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        navigationIcon = navigationIcon,
        actions = navActions
    )
}
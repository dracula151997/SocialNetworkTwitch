package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.dracula.socialnetworktwitch.core.presentation.utils.autoMirror

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.ArrowBack,
    contentDescription: String? = null,
    tint: Color = MaterialTheme.colors.onBackground,
    onBackClicked: () -> Unit,
) {
    IconButton(onClick = onBackClicked, modifier = modifier.autoMirror()) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
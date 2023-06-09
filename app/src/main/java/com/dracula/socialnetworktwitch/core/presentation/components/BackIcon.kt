package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.dracula.socialnetworktwitch.R

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24),
    contentDescription: String? = null,
    tint: Color = MaterialTheme.colors.onBackground,
    onBackClicked: () -> Unit,
) {
    IconButton(onClick = onBackClicked, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}
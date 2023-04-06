package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.TintAwareDrawable
import com.dracula.socialnetworktwitch.presentation.ui.Semantics

@Composable
fun LikeButton(
    isLiked: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick(!isLiked) }
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isLiked) Semantics.ContentDescriptions.UNLIKE else Semantics.ContentDescriptions.LIKE
        )
    }
}
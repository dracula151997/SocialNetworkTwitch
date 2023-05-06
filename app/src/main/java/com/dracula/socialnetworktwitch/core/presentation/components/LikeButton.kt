package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dracula.socialnetworktwitch.core.presentation.Semantics

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
            tint = if (isLiked) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
            contentDescription = if (isLiked) Semantics.ContentDescriptions.UNLIKE else Semantics.ContentDescriptions.LIKE
        )
    }
}
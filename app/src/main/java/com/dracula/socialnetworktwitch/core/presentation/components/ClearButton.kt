package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import com.dracula.socialnetworktwitch.core.presentation.Semantics

@Composable
fun ClearButton(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = Semantics.ContentDescriptions.CLEAR
        )
    }
}
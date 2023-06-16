package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingContent(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        AnimatedContent(targetState = isLoading, label = "") { isLoading ->
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                else -> content()
            }

        }
    }
}
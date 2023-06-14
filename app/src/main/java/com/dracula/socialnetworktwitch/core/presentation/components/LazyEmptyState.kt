package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

fun LazyListScope.EmptyState(
    message: String,
    modifier: Modifier,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = TextStyle.Default,
) {
    item {
        Text(
            text = message,
            modifier = modifier,
            style = style,
            textAlign = textAlign
        )

    }
}
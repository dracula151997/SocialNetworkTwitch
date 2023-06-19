package com.dracula.socialnetworktwitch.core.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily

@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    textStyle: TextStyle = MaterialTheme.typography.h6.copy(
        fontFamily = appFontFamily
    ),
    textAlign: TextAlign = TextAlign.Center,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Text(
            text = errorMessage,
            style = textStyle,
            textAlign = textAlign
        )
    }
}
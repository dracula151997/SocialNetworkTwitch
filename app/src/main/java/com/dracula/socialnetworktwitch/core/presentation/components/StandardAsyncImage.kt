package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun StandardAsyncImage(
    url: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    showLoadingProgress: Boolean = false,
    errorPlaceholder: Painter? = null,

    ) {
    if (showLoadingProgress) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            modifier = modifier,
            contentScale = contentScale,
            contentDescription = contentDescription,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp)
                )
            },
            error = {
                if (errorPlaceholder != null)
                    Image(painter = errorPlaceholder, contentDescription = null)
            }


        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            error = errorPlaceholder,
        )
    }
}
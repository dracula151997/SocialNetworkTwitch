package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dracula.socialnetworktwitch.R

@Composable
fun StandardAsyncImage(
    url: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    errorPlaceholder: Painter? = null,
    placeholder: Painter? = painterResource(id = R.drawable.placeholder),

    ) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        modifier = modifier,
        contentScale = contentScale,
        contentDescription = contentDescription,
        error = errorPlaceholder,
        placeholder = placeholder


    )
}
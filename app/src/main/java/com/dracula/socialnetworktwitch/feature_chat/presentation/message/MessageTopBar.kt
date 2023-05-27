package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MessageTopBar(
    title: String = "Dracula1597",
) {
    TopAppBar(
        title = {
            Text(text = title, fontWeight = FontWeight.Bold)
        },
    )
}
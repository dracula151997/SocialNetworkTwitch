package com.dracula.socialnetworktwitch.presentation.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

@Composable
fun ChatScreen(navController: NavController) {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "Chat Screen")
    }
}
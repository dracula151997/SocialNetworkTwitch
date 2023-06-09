package com.dracula.socialnetworktwitch.core.domain

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val icon: ImageVector? = null,
    val contentDescription: String? = null,
    val alertCount: Int? = null,
) {
    val enabled get() = icon != null && route.isNotEmpty()
}

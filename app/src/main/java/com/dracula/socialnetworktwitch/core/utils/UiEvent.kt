package com.dracula.socialnetworktwitch.core.utils

sealed interface UiEvent {
    data class SnackbarEvent(val uiText: UiText) : UiEvent
    data class Navigate(val route: String) : UiEvent
}
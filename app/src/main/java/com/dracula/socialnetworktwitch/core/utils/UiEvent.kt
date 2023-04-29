package com.dracula.socialnetworktwitch.core.utils

import kotlinx.coroutines.flow.MutableSharedFlow


sealed interface UiEvent {
    data class SnackbarEvent(val uiText: UiText) : UiEvent
    data class Navigate(val route: String) : UiEvent

    object NavigateUp : UiEvent
}
package com.dracula.socialnetworktwitch.core.utils


sealed interface UiEvent {
    data class ShowSnackbar(val uiText: UiText) : UiEvent
    data class Navigate(val route: String) : UiEvent

    object NavigateUp : UiEvent
}
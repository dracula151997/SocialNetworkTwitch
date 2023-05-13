package com.dracula.socialnetworktwitch.core.utils

sealed class UiEvent : BaseUiEvent() {
    data class ShowSnackbar(val uiText: UiText) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object NavigateUp : UiEvent()
}
package com.dracula.socialnetworktwitch.feature_auth.presentation.register

sealed interface RegisterEvent {
    data class OnUserNameEntered(val value: String) : RegisterEvent
    data class OnPasswordEntered(val value: String) : RegisterEvent
    data class OnEmailEntered(val value: String) : RegisterEvent
    object TogglePasswordVisibility : RegisterEvent
    object Register : RegisterEvent
}
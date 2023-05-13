package com.dracula.socialnetworktwitch.feature_auth.presentation.register

sealed interface RegisterAction {
    data class OnUserNameEntered(val value: String) : RegisterAction
    data class OnPasswordEntered(val value: String) : RegisterAction
    data class OnEmailEntered(val value: String) : RegisterAction
    object TogglePasswordVisibility : RegisterAction
    object Register : RegisterAction
}
package com.dracula.socialnetworktwitch.feature_auth.presentation.login

sealed interface LoginEvent {
    data class EmailEntered(val email: String) : LoginEvent
    data class PasswordEntered(val password: String) : LoginEvent
    object TogglePasswordVisibility : LoginEvent
    object Login : LoginEvent
}
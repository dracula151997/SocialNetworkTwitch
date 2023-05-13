package com.dracula.socialnetworktwitch.feature_auth.presentation.login

sealed interface LoginAction {
    data class EmailEntered(val email: String) : LoginAction
    data class PasswordEntered(val password: String) : LoginAction
    object TogglePasswordVisibility : LoginAction
    object Login : LoginAction
}
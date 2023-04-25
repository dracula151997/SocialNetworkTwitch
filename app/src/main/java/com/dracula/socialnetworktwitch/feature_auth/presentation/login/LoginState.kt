package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import com.dracula.socialnetworktwitch.core.utils.UiText

data class LoginState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
) {
    companion object {
        fun loading() = LoginState(isLoading = true)
        fun success() = LoginState(isLoading = false)
        fun error(message: UiText) = LoginState(isLoading = false, error = message)
        fun idle() = LoginState()
    }
}
package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import com.dracula.socialnetworktwitch.core.utils.Error

data class RegisterState(
    val usernameText: String = "",
    val usernameError: UsernameError? = null,
    val emailText: String = "",
    val emailError: EmailError? = null,
    val passwordText: String = "",
    val passwordError: PasswordError? = null,
    val isPasswordToggleVisible: Boolean = false,
) {
    sealed interface UsernameError {
        object FieldEmpty : UsernameError
        object InputTooShort : UsernameError
    }

    sealed interface EmailError {
        object FieldEmpty : EmailError
        object InvalidEmail : EmailError
    }

    sealed interface PasswordError {
        object FieldEmpty : PasswordError
        object InputTooShort : PasswordError
        object InvalidPassword : PasswordError
    }
}

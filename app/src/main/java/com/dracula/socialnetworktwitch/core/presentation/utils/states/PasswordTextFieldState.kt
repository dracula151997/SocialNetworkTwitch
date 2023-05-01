package com.dracula.socialnetworktwitch.core.presentation.utils.states

import com.dracula.socialnetworktwitch.core.utils.ValidationError

data class PasswordTextFieldState(
    val text: String = "",
    val error: ValidationError? = null,
    val isPasswordToggleVisible: Boolean = false,
)
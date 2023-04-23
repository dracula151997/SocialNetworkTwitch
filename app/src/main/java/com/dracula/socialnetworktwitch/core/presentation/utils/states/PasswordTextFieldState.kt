package com.dracula.socialnetworktwitch.core.presentation.utils.states

import com.dracula.socialnetworktwitch.core.utils.Error

data class PasswordTextFieldState(
    val text: String = "",
    val error: Error? = null,
    val isPasswordToggleVisible: Boolean = false,
)
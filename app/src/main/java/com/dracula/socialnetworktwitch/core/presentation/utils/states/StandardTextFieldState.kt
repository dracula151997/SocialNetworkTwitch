package com.dracula.socialnetworktwitch.core.presentation.utils.states

import com.dracula.socialnetworktwitch.core.utils.ValidationError

data class StandardTextFieldState(
    val text: String = "",
    val error: ValidationError? = null
) {
    fun defaultState(): StandardTextFieldState {
        return copy(text = "", error = null)
    }

    val hasText get() = text.isNotEmpty()
    val hasError get() = error != null
}

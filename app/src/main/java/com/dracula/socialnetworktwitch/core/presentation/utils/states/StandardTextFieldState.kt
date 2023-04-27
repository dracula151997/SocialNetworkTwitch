package com.dracula.socialnetworktwitch.core.presentation.utils.states

import com.dracula.socialnetworktwitch.core.utils.Error

data class StandardTextFieldState(
    val text: String = "",
    val error: Error? = null
){
    fun defaultState(): StandardTextFieldState {
        return StandardTextFieldState()
    }
}

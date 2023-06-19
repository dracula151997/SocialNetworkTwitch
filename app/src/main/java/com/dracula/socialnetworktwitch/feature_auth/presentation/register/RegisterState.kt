package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import com.dracula.socialnetworktwitch.core.presentation.utils.UiState

data class RegisterState(
    val isLoading: Boolean = false,
) : UiState()
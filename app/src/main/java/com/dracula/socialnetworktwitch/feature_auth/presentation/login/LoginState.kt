package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import com.dracula.socialnetworktwitch.core.presentation.utils.UiState

data class LoginState(
    val isLoading: Boolean = false,
) : UiState()
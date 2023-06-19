package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class LoginEvent : UiEvent() {
    object Login : LoginEvent()
}
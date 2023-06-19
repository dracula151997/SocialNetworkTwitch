package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class RegisterEvent : UiEvent() {
    object Register : RegisterEvent()
}
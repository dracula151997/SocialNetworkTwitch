package com.dracula.socialnetworktwitch.feature_auth.presentation.register

sealed interface RegisterAction {
    object Register : RegisterAction
}
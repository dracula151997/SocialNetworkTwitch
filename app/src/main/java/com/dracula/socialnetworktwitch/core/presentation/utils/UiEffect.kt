package com.dracula.socialnetworktwitch.core.presentation.utils

import com.dracula.socialnetworktwitch.core.utils.UiText

abstract class UiEffect

sealed class CommonUiEffect : UiEffect() {
    data class ShowSnackbar(val uiText: UiText) : CommonUiEffect()
    data class Navigate(
        val route: String,
    ) : CommonUiEffect()

    object NavigateUp : CommonUiEffect()
}
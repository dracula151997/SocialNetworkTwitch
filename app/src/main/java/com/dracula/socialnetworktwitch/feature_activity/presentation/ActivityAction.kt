package com.dracula.socialnetworktwitch.feature_activity.presentation

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class ActivityAction : UiEvent() {
    data class ClickOnUser(val userId: String) : ActivityAction()
    data class ClickOnParent(val parentId: String) : ActivityAction()
    object Refreshing : ActivityAction()
}
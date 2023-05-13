package com.dracula.socialnetworktwitch.feature_activity.presentation

sealed interface ActivityAction {
    data class ClickOnUser(val userId: String) : ActivityAction
    data class ClickOnParent(val parentId: String) : ActivityAction
}
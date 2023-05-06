package com.dracula.socialnetworktwitch.feature_activity.presentation

sealed interface ActivityEvent {
    data class ClickOnUser(val userId: String) : ActivityEvent
    data class ClickOnParent(val parentId: String) : ActivityEvent
}
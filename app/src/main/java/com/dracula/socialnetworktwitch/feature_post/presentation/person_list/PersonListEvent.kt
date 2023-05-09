package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

sealed interface PersonListEvent {
    data class ToggleFollowStateForUser(val userId: String) : PersonListEvent
}
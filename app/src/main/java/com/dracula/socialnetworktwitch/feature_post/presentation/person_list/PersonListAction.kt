package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

sealed interface PersonListAction {
    data class ToggleFollowStateForUser(val userId: String) : PersonListAction
}
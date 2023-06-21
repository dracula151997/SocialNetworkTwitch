package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class PersonListEvent : UiEvent() {
    data class ToggleFollowStateForUser(val userId: String) : PersonListEvent()
}
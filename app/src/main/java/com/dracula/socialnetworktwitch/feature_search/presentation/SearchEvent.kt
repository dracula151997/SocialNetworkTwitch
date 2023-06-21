package com.dracula.socialnetworktwitch.feature_search.presentation

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class SearchEvent : UiEvent() {
    data class OnSearch(val query: String) : SearchEvent()
    data class ToggleFollowState(val userId: String) : SearchEvent()
    object Refreshing : SearchEvent()
}
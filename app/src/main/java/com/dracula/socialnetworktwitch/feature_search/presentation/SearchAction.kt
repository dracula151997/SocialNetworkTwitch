package com.dracula.socialnetworktwitch.feature_search.presentation

sealed interface SearchAction {
    data class OnSearch(val query: String) : SearchAction
    data class ToggleFollowState(val userId: String) : SearchAction

    object Refreshing : SearchAction
}
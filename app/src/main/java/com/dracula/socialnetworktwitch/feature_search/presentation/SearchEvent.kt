package com.dracula.socialnetworktwitch.feature_search.presentation

sealed interface SearchEvent {
    data class OnSearch(val query: String) : SearchEvent
}
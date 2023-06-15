package com.dracula.socialnetworktwitch.feature_search.presentation

import com.dracula.socialnetworktwitch.core.domain.model.UserItem

data class SearchState(
    val isLoading: Boolean = false,
    val refreshing: Boolean = false,
    val userItems: List<UserItem>? = null,
)
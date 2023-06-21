package com.dracula.socialnetworktwitch.feature_search.presentation

import com.dracula.socialnetworktwitch.core.domain.model.UserItem
import com.dracula.socialnetworktwitch.core.presentation.utils.UiState

data class SearchState(
    val isLoading: Boolean = false,
    val refreshing: Boolean = false,
    val userItems: List<UserItem>? = null,
) : UiState()
package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

import com.dracula.socialnetworktwitch.core.domain.model.UserItem
import com.dracula.socialnetworktwitch.core.presentation.utils.UiState

data class PersonListState(
    val isLoading: Boolean = false,
    val users: List<UserItem> = emptyList(),
) : UiState() {
    val hasNoUsers: Boolean get() = users.isEmpty()
}
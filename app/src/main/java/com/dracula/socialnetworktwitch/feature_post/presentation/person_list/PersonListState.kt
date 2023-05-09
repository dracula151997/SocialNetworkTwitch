package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

import com.dracula.socialnetworktwitch.core.domain.model.UserItem

data class PersonListState(
    val isLoading: Boolean = false,
    val users: List<UserItem> = emptyList(),
) {
    val hasNoUsers: Boolean get() = users.isEmpty()
}
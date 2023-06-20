package com.dracula.socialnetworktwitch.feature_profile.profile

import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.presentation.utils.UiState
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val refreshing: Boolean = false,
    val data: Profile? = null,
    val showLogoutDialog: Boolean = false,
    val showMorePopupMenu: Boolean = false,
    val pagingState: PagingState<Post> = PagingState()
) : UiState()

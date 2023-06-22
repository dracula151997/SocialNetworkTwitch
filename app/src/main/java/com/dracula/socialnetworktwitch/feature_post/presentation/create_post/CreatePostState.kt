package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import com.dracula.socialnetworktwitch.core.presentation.utils.UiState

data class CreatePostState(
    val isLoading: Boolean = false,
) : UiState()
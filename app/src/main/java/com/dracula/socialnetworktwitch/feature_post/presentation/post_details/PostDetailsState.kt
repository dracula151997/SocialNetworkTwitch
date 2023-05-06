package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

import com.dracula.socialnetworktwitch.core.domain.model.Comment
import com.dracula.socialnetworktwitch.core.domain.model.Post

data class PostDetailsState(
    val isPostLoading: Boolean = false,
    val isLoadingComments: Boolean = false,
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
)
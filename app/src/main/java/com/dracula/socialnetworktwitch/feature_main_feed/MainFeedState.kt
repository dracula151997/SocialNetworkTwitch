package com.dracula.socialnetworktwitch.feature_main_feed

import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_post.domain.Post

data class MainFeedState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val page: Int = Constants.DEFAULT_PAGE,
)

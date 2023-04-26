package com.dracula.socialnetworktwitch.feature_main_feed

import com.dracula.socialnetworktwitch.core.utils.Constants

data class MainFeedState(
    val isLoadingFirstTime: Boolean = true,
    val isLoadingNewPost: Boolean = false,
    val page: Int = Constants.DEFAULT_PAGE,
)

package com.dracula.socialnetworktwitch.feature_main_feed

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class MainFeedEvent : UiEvent() {
    data class LikePost(val postId: String) : MainFeedEvent()
    data class DeletePost(val postId: String) : MainFeedEvent()
    object LoadNextPosts : MainFeedEvent()
    object Refresh : MainFeedEvent()
}
package com.dracula.socialnetworktwitch.feature_main_feed

sealed interface MainFeedAction {
    object LoadMorePosts : MainFeedAction
    object LoadedPage : MainFeedAction
    data class LikePost(val postId: String) : MainFeedAction
    data class DeletePost(val postId: String) : MainFeedAction
}
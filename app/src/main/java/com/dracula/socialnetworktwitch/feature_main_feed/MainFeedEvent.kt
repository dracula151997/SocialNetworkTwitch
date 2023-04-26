package com.dracula.socialnetworktwitch.feature_main_feed

sealed interface MainFeedEvent {
    object LoadMorePosts: MainFeedEvent
    object LoadedPage: MainFeedEvent
}
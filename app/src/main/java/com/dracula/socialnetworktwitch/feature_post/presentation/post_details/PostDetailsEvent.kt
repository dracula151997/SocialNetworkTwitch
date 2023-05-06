package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

sealed interface PostDetailsEvent {
    object LikePost : PostDetailsEvent
    data class Comment(val commentId: String) : PostDetailsEvent
    data class LikeComment(val commentId: String) : PostDetailsEvent
    object SharePost : PostDetailsEvent

}
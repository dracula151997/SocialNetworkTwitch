package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

sealed interface PostDetailsEvent {
    object LikePost : PostDetailsEvent
    object Comment : PostDetailsEvent
    data class LikeComment(val commentId: String) : PostDetailsEvent
    object SharePost : PostDetailsEvent

    data class CommentEntered(val commentText: String) : PostDetailsEvent

}
package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

sealed interface PostDetailsAction {
    object LikePost : PostDetailsAction
    object Comment : PostDetailsAction
    data class LikeComment(val commentId: String) : PostDetailsAction
    object SharePost : PostDetailsAction

    data class CommentEntered(val commentText: String) : PostDetailsAction

}
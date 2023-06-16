package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

data class PostDetailsAction(
    val onLikePostClicked: () -> Unit = {},
    val onLikeCommentClicked: (commentId: String) -> Unit = {},
    val onSendClicked: () -> Unit = {},
)
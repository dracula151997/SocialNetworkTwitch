package com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.request

data class CreateCommentRequest(
    val comment: String,
    val postId: String,
)

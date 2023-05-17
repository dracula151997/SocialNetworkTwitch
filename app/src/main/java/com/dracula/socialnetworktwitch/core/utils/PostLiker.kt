package com.dracula.socialnetworktwitch.core.utils

import com.dracula.socialnetworktwitch.core.domain.model.Post

interface PostLiker {
    suspend fun likePost(
        posts: List<Post>,
        postId: String,
        onRequest: suspend (isLiked: Boolean) -> UnitApiResult,
        onStateUpdated: (posts: List<Post>) -> Unit,
        onError: () -> Unit
    )
}
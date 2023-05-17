package com.dracula.socialnetworktwitch.core.utils

import com.dracula.socialnetworktwitch.core.domain.model.Post

class DefaultPostLiker : PostLiker {
    override suspend fun likePost(
        posts: List<Post>,
        postId: String,
        onRequest: suspend (Boolean) -> UnitApiResult,
        onStateUpdated: (posts: List<Post>) -> Unit,
        onError: () -> Unit
    ) {
        val currentlyLiked = posts.find { it.id == postId }?.isLiked == true
        val newPosts = posts.map {
            if (it.id == postId) it.copy(
                isLiked = !it.isLiked,
                likeCount = if (currentlyLiked) it.likeCount.dec() else it.likeCount.inc()
            )
            else it
        }
        onStateUpdated(newPosts)
        when (val result = onRequest(currentlyLiked)) {
            is ApiResult.Success -> Unit

            is ApiResult.Error -> {
                val oldPosts = posts.map {
                    if (it.id == postId) it.copy(
                        isLiked = currentlyLiked,
                        likeCount = if (currentlyLiked) it.likeCount.dec() else it.likeCount.inc()
                    )
                    else it
                }
                onStateUpdated(oldPosts)
                onError()
            }
        }
    }

}
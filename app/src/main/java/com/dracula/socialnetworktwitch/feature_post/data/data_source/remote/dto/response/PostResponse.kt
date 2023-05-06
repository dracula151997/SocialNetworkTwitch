package com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.response

import com.dracula.socialnetworktwitch.core.domain.model.Post

data class PostResponse(
    val id: String,
    val userId: String,
    val username: String,
    val imageUrl: String,
    val profilePictureUrl: String,
    val description: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isOwnPost: Boolean
) {
    fun toPost(): Post {
        return Post(
            id = id,
            userId = userId,
            username = username,
            imageUrl = imageUrl,
            profileImageUrl = profilePictureUrl,
            description = description,
            likeCount = likeCount,
            commentCount = commentCount,
            isLiked = isLiked,
            isOwnPost = isOwnPost,
        )
    }
}

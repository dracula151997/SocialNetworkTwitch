package com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.response

import com.dracula.socialnetworktwitch.core.domain.model.Comment
import com.dracula.socialnetworktwitch.core.utils.toFormattedDate

data class CommentResponse(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val timestamp: Long,
    val comment: String,
    val isLiked: Boolean,
    val likeCount: Int
) {
    fun toComment(): Comment {
        return Comment(
            id = id,
            username = username,
            profilePictureUrl = profilePictureUrl,
            formattedTime = timestamp.toFormattedDate("MMM dd, HH:mm"),
            comment = comment,
            isLiked = isLiked,
            likeCount = likeCount
        )
    }
}

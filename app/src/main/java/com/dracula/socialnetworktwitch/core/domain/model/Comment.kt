package com.dracula.socialnetworktwitch.core.domain.model

data class Comment(
    val commentId: Int,
    val commentText: String,
    val isLiked: Boolean = false,
    val likeCount: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val username: String,
    val usernameProfileUrl: String,
) {
    companion object {
        fun dummy(): Comment {
            return Comment(
                commentText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Amet est placerat in egestas erat imperdiet sed euismod nisi.",
                isLiked = true,
                likeCount = 5,
                username = "Philip Lackner",
                usernameProfileUrl = "", commentId = 8994
            )
        }
    }
}
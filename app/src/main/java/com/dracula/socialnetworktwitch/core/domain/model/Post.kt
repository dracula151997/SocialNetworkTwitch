package com.dracula.socialnetworktwitch.core.domain.model

data class Post(
    val id: String,
    val userId: String,
    val username: String,
    val imageUrl: String,
    val profileImageUrl: String,
    val description: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isOwnPost: Boolean
)
package com.dracula.socialnetworktwitch.core.data.remote.dto.response

import com.dracula.socialnetworktwitch.core.domain.model.UserItem

data class UserItemResponse(
    val userId: String,
    val username: String,
    val profilePictureUrl: String,
    val bio: String,
    val isFollowing: Boolean
) {
    fun toUserItem(): UserItem {
        return UserItem(userId, username, profilePictureUrl, bio, isFollowing)
    }
}
package com.dracula.socialnetworktwitch.feature_profile.domain.model

import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response.UserResponse

data class UserItem(
    val userId: String,
    val username: String,
    val profilePictureUrl: String,
    val bio: String,
    val isFollowing: Boolean
)

fun UserResponse.toUserItem(): UserItem {
    return UserItem(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        bio = bio,
        isFollowing = isFollowing
    )
}
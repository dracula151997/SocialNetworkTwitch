package com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response

import com.dracula.socialnetworktwitch.core.domain.model.User

data class UserResponse(
    val userId: String,
    val username: String,
    val profilePictureUrl: String,
    val bio: String,
    val isFollowing: Boolean
)
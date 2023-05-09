package com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.request

data class LikeUpdateRequest(
    val parentId: String,
    val parentType: Int
)

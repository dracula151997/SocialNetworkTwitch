package com.dracula.socialnetworktwitch.feature_activity.domain.model

import com.dracula.socialnetworktwitch.core.utils.ActivityType

data class Activity(
    val userId: String,
    val parentId: String,
    val username: String,
    val actionType: ActivityType,
    val formattedTime: String,
)
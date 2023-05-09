package com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.dto

import com.dracula.socialnetworktwitch.core.utils.ActivityType
import com.dracula.socialnetworktwitch.core.utils.toFormattedDate
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity

data class ActivityResponse(
    val timestamp: Long,
    val userId: String,
    val parentId: String,
    val type: Int,
    val username: String,
    val id: String
) {
    fun toActivity(): Activity {
        return Activity(
            userId = userId,
            parentId = parentId,
            username = username,
            actionType = ActivityType.getActivityType(type),
            formattedTime = timestamp.toFormattedDate("MMM dd, HH:mm")
        )
    }
}

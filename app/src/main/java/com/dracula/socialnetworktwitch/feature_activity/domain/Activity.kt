package com.dracula.socialnetworktwitch.feature_activity.domain

import com.dracula.socialnetworktwitch.core.utils.toFormattedString
import kotlin.random.Random

data class Activity(
    val id: Int,
    val text: String,
    val username: String,
    val actionType: ActivityAction,
    val timestamp: String,
) {

    val isFollowedYouActionType
        get() = actionType == ActivityAction.FollowedYou

    companion object {
        fun dummy(): Activity {
            return Activity(
                id = 1,
                text = "docendi",
                timestamp = System.currentTimeMillis().toFormattedString("MMM dd, hh:mm"),
                username = "Philip Lackner",
                actionType = getActivityType()
            )
        }

        private fun getActivityType(): ActivityAction {
            return if (Random.nextInt(2) == 0) {
                ActivityAction.LikedPost
            } else ActivityAction.CommentOnPost
        }
    }
}
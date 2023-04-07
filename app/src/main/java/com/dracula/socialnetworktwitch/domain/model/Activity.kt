package com.dracula.socialnetworktwitch.domain.model

import com.dracula.socialnetworktwitch.core.utils.toFormattedString
import kotlin.random.Random

data class Activity(
    val id: Int,
    val text: String,
    val username: String,
    val actionType: ActivityAction,
    val timestamp: String,
) {
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
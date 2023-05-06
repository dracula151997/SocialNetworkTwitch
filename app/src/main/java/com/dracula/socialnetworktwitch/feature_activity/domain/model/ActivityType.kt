package com.dracula.socialnetworktwitch.feature_activity.domain.model

sealed class ActivityType(val type: Int) {
    object LikedPost : ActivityType(0)
    object LikedComment : ActivityType(1)
    object CommentedOnPost : ActivityType(2)
    object FollowedUser : ActivityType(3)

    companion object {
        fun getActivityType(type: Int): ActivityType {
            return when (type) {
                LikedPost.type -> return LikedPost
                LikedComment.type -> return LikedComment
                CommentedOnPost.type -> return CommentedOnPost
                FollowedUser.type -> return FollowedUser
                else -> FollowedUser
            }
        }
    }

}

package com.dracula.socialnetworktwitch.feature_activity.domain

sealed interface ActivityAction {
    object LikedPost : ActivityAction
    object CommentOnPost : ActivityAction
    object FollowedYou : ActivityAction
}

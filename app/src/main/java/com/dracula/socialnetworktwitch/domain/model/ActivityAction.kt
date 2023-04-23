package com.dracula.socialnetworktwitch.domain.model

sealed interface ActivityAction {
    object LikedPost : ActivityAction
    object CommentOnPost : ActivityAction
    object FollowedYou : ActivityAction
}

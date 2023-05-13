package com.dracula.socialnetworktwitch.feature_profile.profile

sealed interface ProfileScreenAction {
    data class GetProfile(val userId: String?) : ProfileScreenAction
    data class ToggleLikeForPost(val postId: String) : ProfileScreenAction

}
package com.dracula.socialnetworktwitch.feature_profile.profile

sealed interface ProfileScreenAction {
    data class GetProfile(val userId: String?) : ProfileScreenAction
    data class LikePost(val postId: String) : ProfileScreenAction

    object ShowLogoutDialog : ProfileScreenAction

    object HideLogoutDialog : ProfileScreenAction

    object Logout : ProfileScreenAction

    object ShowMorePopupMenu : ProfileScreenAction

    data class DeletePost(val postId: String) : ProfileScreenAction

    data class ToggleFollowStateForUser(val userId: String) : ProfileScreenAction
}
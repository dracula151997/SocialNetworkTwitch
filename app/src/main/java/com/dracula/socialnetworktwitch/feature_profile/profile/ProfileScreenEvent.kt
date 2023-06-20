package com.dracula.socialnetworktwitch.feature_profile.profile

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class ProfileScreenEvent : UiEvent() {
    data class GetProfile(val userId: String?) : ProfileScreenEvent()
    data class LikePost(val postId: String) : ProfileScreenEvent()

    object ShowLogoutDialog : ProfileScreenEvent()

    object HideLogoutDialog : ProfileScreenEvent()

    object Logout : ProfileScreenEvent()

    data class Refreshing(val userId: String?) : ProfileScreenEvent()

    object ShowMorePopupMenu : ProfileScreenEvent()

    data class DeletePost(val postId: String) : ProfileScreenEvent()

    data class ToggleFollowStateForUser(val userId: String) : ProfileScreenEvent()

    object LoadNextPosts : ProfileScreenEvent()
}
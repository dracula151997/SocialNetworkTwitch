package com.dracula.socialnetworktwitch.feature_profile.profile

sealed interface ProfileEvent{
    data class GetProfile(val userId: String?) : ProfileEvent
}
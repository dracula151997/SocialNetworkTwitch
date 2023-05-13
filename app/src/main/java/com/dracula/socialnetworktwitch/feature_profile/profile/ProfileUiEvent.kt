package com.dracula.socialnetworktwitch.feature_profile.profile

import com.dracula.socialnetworktwitch.core.utils.BaseUiEvent

sealed class ProfileUiEvent : BaseUiEvent() {
    object PostLiked : ProfileUiEvent()
}

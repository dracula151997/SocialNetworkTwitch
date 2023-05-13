package com.dracula.socialnetworktwitch.feature_main_feed

import com.dracula.socialnetworktwitch.core.utils.BaseUiEvent

sealed class MainFeedUiEvent : BaseUiEvent() {
    object LikedPost : MainFeedUiEvent()

}

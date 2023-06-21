package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class MessageScreenAction : UiEvent() {
    object SendMessage : MessageScreenAction()
    object GetMessagesForChat : MessageScreenAction()
}
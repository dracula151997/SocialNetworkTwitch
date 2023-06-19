package com.dracula.socialnetworktwitch.feature_chat.presentation.message

sealed interface MessageScreenAction {
    object SendMessage : MessageScreenAction
    object GetMessagesForChat : MessageScreenAction
}
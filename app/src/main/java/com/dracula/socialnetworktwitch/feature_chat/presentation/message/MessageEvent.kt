package com.dracula.socialnetworktwitch.feature_chat.presentation.message

sealed interface MessageEvent {
    data class EnteredMessage(val message: String) : MessageEvent
    object SendMessage : MessageEvent
    object GetMessagesForChat : MessageEvent
}
package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import com.dracula.socialnetworktwitch.feature_chat.domain.model.Chat

data class ChatState(
    val isLoading: Boolean = true,
    val refreshing: Boolean = false,
    val chats: List<Chat> = emptyList(),
)
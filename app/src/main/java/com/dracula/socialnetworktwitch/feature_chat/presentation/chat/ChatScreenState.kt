package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import com.dracula.socialnetworktwitch.core.presentation.utils.UiState
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Chat

data class ChatScreenState(
    val isLoading: Boolean = true,
    val refreshing: Boolean = false,
    val chats: List<Chat> = emptyList(),
) : UiState()
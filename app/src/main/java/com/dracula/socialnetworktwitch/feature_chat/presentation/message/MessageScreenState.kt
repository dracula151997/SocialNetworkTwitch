package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import com.dracula.socialnetworktwitch.feature_chat.domain.Message

data class MessageScreenState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,

    )
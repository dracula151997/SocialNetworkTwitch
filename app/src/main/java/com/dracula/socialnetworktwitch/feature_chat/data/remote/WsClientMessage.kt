package com.dracula.socialnetworktwitch.feature_chat.data.remote

data class WsClientMessage(
    val toId: String,
    val text: String,
    val chatId: String?,
)
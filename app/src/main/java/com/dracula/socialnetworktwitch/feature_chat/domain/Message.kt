package com.dracula.socialnetworktwitch.feature_chat.domain

data class Message(
    val fromId: String,
    val toId: String,
    val text: String,
    val formattedTime: String,
    val chatId: String,
    val id: String,

    )

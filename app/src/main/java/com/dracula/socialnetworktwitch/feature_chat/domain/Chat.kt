package com.dracula.socialnetworktwitch.feature_chat.domain

data class Chat(
    val username: String,
    val profilePictureUrl: String,
    val lastMessage: String,
    val lastMessageFormattedTimestamp: String,
)
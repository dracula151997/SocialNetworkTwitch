package com.dracula.socialnetworktwitch.feature_chat.data.remote.dto

import com.dracula.socialnetworktwitch.core.utils.toFormattedDate
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Chat

data class ChatResponse(
    val chatId: String,
    val remoteUserId: String?,
    val remoteUsername: String?,
    val remoteUserProfilePictureUrl: String?,
    val lastMessage: String?,
    val timestamp: Long?,
) {
    fun toChat(): Chat {
        return Chat(
            chatId = chatId,
            remoteUserId = remoteUserId.orEmpty(),
            remoteUsername = remoteUsername.orEmpty(),
            remoteUserProfilePictureUrl = remoteUserProfilePictureUrl.orEmpty(),
            lastMessage = lastMessage.orEmpty(),
            formattedTimestamp = timestamp?.toFormattedDate("dd MMM h:mma").orEmpty()
        )
    }
}
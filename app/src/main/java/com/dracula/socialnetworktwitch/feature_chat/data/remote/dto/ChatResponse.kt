package com.dracula.socialnetworktwitch.feature_chat.data.remote.dto

import com.dracula.socialnetworktwitch.feature_chat.domain.model.Chat
import java.text.SimpleDateFormat
import java.util.Locale

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
            formattedTimestamp = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(timestamp)
        )
    }
}
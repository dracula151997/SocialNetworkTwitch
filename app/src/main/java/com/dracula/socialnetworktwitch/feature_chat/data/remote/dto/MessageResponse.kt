package com.dracula.socialnetworktwitch.feature_chat.data.remote.dto

import android.icu.text.SimpleDateFormat
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Message
import java.util.Locale

data class MessageResponse(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?,
    val id: String
) {
    fun toMessage(): Message {
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                timestamp
            ),
            chatId = chatId.orEmpty(),
            id = id
        )
    }
}


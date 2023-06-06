package com.dracula.socialnetworktwitch.feature_chat.data.remote.dto

import com.dracula.socialnetworktwitch.core.utils.toFormattedDate
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Message

data class WsServerMessage(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?,
) {
    fun toMessage(): Message {
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            formattedTime = timestamp.toFormattedDate("dd MMM h:mma"),
            chatId = chatId.orEmpty(),
            id = ""
        )
    }
}
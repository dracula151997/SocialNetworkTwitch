package com.dracula.socialnetworktwitch.feature_chat.data.remote

import com.dracula.socialnetworktwitch.feature_chat.data.remote.dto.WsClientMessage
import com.dracula.socialnetworktwitch.feature_chat.data.remote.dto.WsServerMessage
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel


interface ChatWebSocketService {

    @Receive
    fun observeEvents(): ReceiveChannel<WebSocket.Event>

    @Send
    fun sendMessage(message: WsClientMessage)

    @Receive
    fun observeMessages(): ReceiveChannel<WsServerMessage>
}
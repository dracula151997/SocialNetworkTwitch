package com.dracula.socialnetworktwitch.feature_chat.data.remote.utils

import com.dracula.socialnetworktwitch.feature_chat.data.remote.ChatWebSocketService
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient
import kotlin.time.Duration.Companion.seconds

object ScarletInstance {

    var instance: ChatWebSocketService? = null

    fun init(okHttpClient: OkHttpClient): ChatWebSocketService {
        return createScarlet(okHttpClient)
    }

    fun cleanup() {
        instance = null
    }

    private fun createScarlet(client: OkHttpClient): ChatWebSocketService =
        Scarlet.Builder()
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
            .webSocketFactory(client.newWebSocketFactory("ws://10.0.2.2:8080/api/chat/websocket"))
            .backoffStrategy(LinearBackoffStrategy(5.seconds.inWholeMilliseconds))
            .build()
            .create()
}
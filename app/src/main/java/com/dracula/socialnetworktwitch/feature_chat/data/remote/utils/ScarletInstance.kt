package com.dracula.socialnetworktwitch.feature_chat.data.remote.utils

import com.dracula.socialnetworktwitch.feature_chat.data.remote.ChatWebSocketService
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient
import kotlin.time.Duration.Companion.seconds

class ScarletInstance private constructor() {
    companion object {
        @Volatile
        private lateinit var instance: ChatWebSocketService

        fun getInstance(client: OkHttpClient): ChatWebSocketService {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = Scarlet.Builder()
                        .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
                        .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
                        .webSocketFactory(client.newWebSocketFactory("ws://10.0.2.2:8080/api/chat/websocket"))
                        .backoffStrategy(LinearBackoffStrategy(5.seconds.inWholeMilliseconds))
                        .build()
                        .create()
                }
                return instance
            }
        }

    }

}
package com.dracula.socialnetworktwitch.feature_chat.data.remote.utils

import android.content.Context
import com.dracula.socialnetworktwitch.core.data.remote.interceptors.HeadersInterceptor
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_chat.data.remote.ChatWebSocketService
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.time.Duration.Companion.seconds

object ScarletInstance {

    var instance: ChatWebSocketService? = null

    fun init(context: Context): ChatWebSocketService? {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HeadersInterceptor(
                    context.getSharedPreferences(
                        Constants.DEFAULT_SHARED_PREFERENCE_NAME,
                        Context.MODE_PRIVATE
                    )
                )
            )
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
            .build()
        instance = createScarlet(okHttpClient)
        return instance
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
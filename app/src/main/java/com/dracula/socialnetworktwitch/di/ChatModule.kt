package com.dracula.socialnetworktwitch.di

import android.app.Application
import com.dracula.socialnetworktwitch.feature_chat.data.remote.utils.CustomGsonMessageAdapter
import com.dracula.socialnetworktwitch.feature_chat.data.remote.utils.FlowStreamAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    @Provides
    @Singleton
    fun provideScarlet(
        client: OkHttpClient,
        app: Application
    ): Scarlet {
        return Scarlet.Builder()
            .addStreamAdapterFactory(FlowStreamAdapter.Factory)
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
            .webSocketFactory(client.newWebSocketFactory("ws://192.168.0.2:8001/api/chat/websocket"))
            .lifecycle(AndroidLifecycle.ofApplicationForeground(app))
            .build()
    }
}
package com.dracula.socialnetworktwitch.di

import android.app.Application
import com.dracula.socialnetworktwitch.feature_chat.data.ChatRepositoryImpl
import com.dracula.socialnetworktwitch.feature_chat.data.remote.ChatApi
import com.dracula.socialnetworktwitch.feature_chat.data.remote.ChatWebSocketService
import com.dracula.socialnetworktwitch.feature_chat.data.remote.utils.CustomGsonMessageAdapter
import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
            .webSocketFactory(client.newWebSocketFactory("ws://10.0.2.2:8080/api/chat/websocket"))
//            .lifecycle(AndroidLifecycle.ofApplicationForeground(app))
            .build()
    }

    @Provides
    @Singleton
    fun provideChatService(scarlet: Scarlet): ChatWebSocketService {
        return scarlet.create()
    }

    @Provides
    @Singleton
    fun provideChatApi(client: OkHttpClient): ChatApi {
        return Retrofit.Builder()
            .baseUrl(ChatApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatApi: ChatApi,
        chatWebSocketService: ChatWebSocketService
    ): ChatRepository {
        return ChatRepositoryImpl(
            chatApi = chatApi,
            chatWebSocketService = chatWebSocketService
        )
    }
}
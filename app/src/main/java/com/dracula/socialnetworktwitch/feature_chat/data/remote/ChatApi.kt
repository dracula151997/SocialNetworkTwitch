package com.dracula.socialnetworktwitch.feature_chat.data.remote

import com.dracula.socialnetworktwitch.feature_chat.data.remote.dto.ChatResponse
import com.dracula.socialnetworktwitch.feature_chat.data.remote.dto.MessageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatApi {

    @GET("/api/chats")
    suspend fun getChatForUser(): List<ChatResponse>

    @GET("/api/chat/messages")
    suspend fun getMessagesForChat(
        @Query("chatId") chatId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<MessageResponse>

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }
}
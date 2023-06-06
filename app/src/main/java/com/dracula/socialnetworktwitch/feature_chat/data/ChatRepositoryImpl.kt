package com.dracula.socialnetworktwitch.feature_chat.data

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.feature_chat.data.remote.ChatApi
import com.dracula.socialnetworktwitch.feature_chat.data.remote.ChatWebSocketService
import com.dracula.socialnetworktwitch.feature_chat.data.remote.dto.WsClientMessage
import com.dracula.socialnetworktwitch.feature_chat.data.remote.utils.ScarletInstance
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Chat
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Message
import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.OkHttpClient
import okio.IOException
import retrofit2.HttpException

class ChatRepositoryImpl(
    private val chatApi: ChatApi,
    private val okHttpClient: OkHttpClient,
) : ChatRepository {

    private lateinit var chatService: ChatWebSocketService
    override fun initialize() {
        chatService = ScarletInstance.getInstance(client = okHttpClient)
    }

    override suspend fun getChatsForUser(): ApiResult<List<Chat>> {
        return try {
            val chats = chatApi.getChatForUser().map { it.toChat() }
            ApiResult.Success(data = chats)
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): ApiResult<List<Message>> {
        return try {
            val messages = chatApi.getMessagesForChat(chatId, page, pageSize)
            ApiResult.Success(data = messages.map { it.toMessage() })
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        }
    }

    override fun observeChatEvents(): Flow<WebSocket.Event> {
        return chatService.observeEvents().receiveAsFlow()
    }

    override fun observeMessages(): Flow<Message> {
        return chatService.observeMessages()
            .receiveAsFlow()
            .map { it.toMessage() }
    }

    override fun sendMessage(toId: String, text: String, chatId: String?) {
        chatService.sendMessage(WsClientMessage(toId, text, chatId))
    }

}
package com.dracula.socialnetworktwitch.feature_chat.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants.DEFAULT_PAGE_SIZE
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Message
import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
data class GetMessagesForChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        page: Int,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): ApiResult<List<Message>> {
        return repository.getMessagesForChat(chatId, page, pageSize)
    }
}
package com.dracula.socialnetworktwitch.feature_chat.domain.use_case

import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(text: String, toId: String, chatId: String?) {
        if (text.isEmpty()) return
        chatRepository.sendMessage(
            text = text,
            toId = toId,
            chatId = chatId
        )
    }
}
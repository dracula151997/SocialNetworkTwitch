package com.dracula.socialnetworktwitch.feature_chat.domain.use_case

import com.dracula.socialnetworktwitch.feature_chat.domain.model.Message
import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ObserveMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<Message> {
        return chatRepository.observeMessages()
    }
}
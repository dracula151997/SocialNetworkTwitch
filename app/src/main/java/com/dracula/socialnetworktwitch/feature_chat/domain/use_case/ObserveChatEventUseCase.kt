package com.dracula.socialnetworktwitch.feature_chat.domain.use_case

import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ObserveChatEventUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<WebSocket.Event> {
        return chatRepository.observeChatEvents()
    }
}
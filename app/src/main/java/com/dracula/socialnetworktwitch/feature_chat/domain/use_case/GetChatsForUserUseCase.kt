package com.dracula.socialnetworktwitch.feature_chat.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Chat
import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetChatsForUserUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): ApiResult<List<Chat>> {
        return chatRepository.getChatsForUser()
    }
}
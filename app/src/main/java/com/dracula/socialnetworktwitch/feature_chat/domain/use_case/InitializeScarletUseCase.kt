package com.dracula.socialnetworktwitch.feature_chat.domain.use_case

import com.dracula.socialnetworktwitch.feature_chat.domain.repository.ChatRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class InitializeScarletUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() {
        repository.initialize()
    }
}
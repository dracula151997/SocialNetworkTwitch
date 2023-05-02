package com.dracula.socialnetworktwitch.core.domain.use_cases

import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository

class GetOwnUserIdUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): String {
        return repository.ownUserId
    }
}
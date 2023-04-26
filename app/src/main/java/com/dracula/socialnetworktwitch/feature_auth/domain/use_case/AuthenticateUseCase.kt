package com.dracula.socialnetworktwitch.feature_auth.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository

class AuthenticateUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): UnitApiResult {
        return repository.authenticate()
    }
}
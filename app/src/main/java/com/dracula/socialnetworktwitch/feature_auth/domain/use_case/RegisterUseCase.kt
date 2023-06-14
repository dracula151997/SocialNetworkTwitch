package com.dracula.socialnetworktwitch.feature_auth.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String, username: String, password: String
    ): UnitApiResult {
        return repository.register(
            email = email.trim(),
            username = username.trim(),
            password = password.trim()
        )
    }
}
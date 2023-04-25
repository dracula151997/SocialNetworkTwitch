package com.dracula.socialnetworktwitch.feature_auth.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.isNotNull
import com.dracula.socialnetworktwitch.feature_auth.domain.model.LoginResult
import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthError

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        val trimmedEmail = email.trim()
        val emailError = if (trimmedEmail.isBlank()) AuthError.FieldEmpty else null
        val passwordError = if (password.isBlank()) AuthError.FieldEmpty else null

        if (emailError.isNotNull() || passwordError.isNotNull())
            return LoginResult(emailError = emailError, passwordError = passwordError)

        val result = repository.login(email, password)

        return LoginResult(result = result)
    }
}
package com.dracula.socialnetworktwitch.feature_auth.domain.use_case

import com.dracula.socialnetworktwitch.core.domain.utils.ValidationUtil
import com.dracula.socialnetworktwitch.core.utils.isNotNull
import com.dracula.socialnetworktwitch.feature_auth.domain.model.RegisterResult
import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String, username: String, password: String
    ): RegisterResult {
        val trimmedEmail = email.trim()
        val trimmedUsername = username.trim()
        val emailError = ValidationUtil.validateEmail(trimmedEmail)
        val usernameError = ValidationUtil.validateUsername(trimmedUsername)
        val passwordError = ValidationUtil.validatePassword(password)

        if (emailError.isNotNull() || usernameError.isNotNull() || passwordError.isNotNull()) {
            return RegisterResult(
                emailError = emailError,
                usernameError = usernameError,
                passwordError = passwordError
            )
        }

        val result = repository.register(email.trim(), username.trim(), password.trim())

        return RegisterResult(
            result = result
        )
    }
}
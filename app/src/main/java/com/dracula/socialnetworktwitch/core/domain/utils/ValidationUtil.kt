package com.dracula.socialnetworktwitch.core.domain.utils

import android.util.Patterns
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthError

object ValidationUtil {
    fun validateEmail(email: String): AuthError? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            return AuthError.FieldEmpty
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches())
            return AuthError.InvalidEmail
        return null
    }

    fun validateUsername(username: String): AuthError? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank())
            return AuthError.FieldEmpty
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH)
            return AuthError.InputTooShort

        return null
    }

    fun validatePassword(password: String): AuthError? {
        if (password.isBlank())
            return AuthError.FieldEmpty
        if (password.length < Constants.MIN_PASSWORD_LENGTH)
            return AuthError.InputTooShort
        val capitalLetterInPassword = password.any { it.isUpperCase() }
        val numberInPassword = password.any { it.isDigit() }
        if (!capitalLetterInPassword || !numberInPassword) {
            return AuthError.InvalidPassword
        }

        return null
    }

    fun validateIsNullOrEmpty(value: String?): Boolean {
        return value.isNullOrEmpty()
    }
}
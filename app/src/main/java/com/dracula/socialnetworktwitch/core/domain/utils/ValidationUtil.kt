package com.dracula.socialnetworktwitch.core.domain.utils

import android.util.Patterns
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthValidationError

object ValidationUtil {

    fun validateEmail(email: String): AuthValidationError? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            return AuthValidationError.FieldEmpty
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches())
            return AuthValidationError.InvalidEmail
        return null
    }

    fun validateUsername(username: String): AuthValidationError? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank())
            return AuthValidationError.FieldEmpty
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH)
            return AuthValidationError.InputTooShort

        return null
    }

    fun validatePassword(password: String): AuthValidationError? {
        if (password.isBlank())
            return AuthValidationError.FieldEmpty
        if (password.length < Constants.MIN_PASSWORD_LENGTH)
            return AuthValidationError.InputTooShort
        val capitalLetterInPassword = password.any { it.isUpperCase() }
        val numberInPassword = password.any { it.isDigit() }
        if (!capitalLetterInPassword || !numberInPassword) {
            return AuthValidationError.InvalidPassword
        }

        return null
    }

    fun validateIsNullOrEmpty(value: String?): Boolean {
        return value.isNullOrEmpty()
    }

    fun isGithubLinkValid(link: String?): Boolean {
        if (!link.isNullOrEmpty())
            return link.startsWith("https://github.com") || link.startsWith("http://github.com")
                    || link.startsWith("github.com")

        return true

    }

    fun isValidLink(link: String): Boolean {
        return Patterns.WEB_URL.matcher(link).matches()
    }
}
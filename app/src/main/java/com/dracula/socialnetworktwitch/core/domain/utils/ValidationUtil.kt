package com.dracula.socialnetworktwitch.core.domain.utils

import android.util.Patterns
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthValidationError
import com.dracula.socialnetworktwitch.feature_post.domain.model.CreateCommentValidationError
import java.util.regex.Pattern

fun String.isValidEmail(): Boolean {
    return Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), this.trim())
}

fun String.matchesMinLength(minLength: Int): Boolean {
    return this.trim().length >= minLength
}

fun String.isValidUsername(): Boolean {
    return this.matchesMinLength(minLength = Constants.MIN_USERNAME_LENGTH)
}

fun String.atLeastOneDigits(): Boolean {
    return this.any { it.isDigit() }
}

fun String.atLeastOneUppercaseLetter(): Boolean {
    return this.any { it.isUpperCase() }
}

fun String.isValidPassword(): Boolean {
    return matchesMinLength(minLength = Constants.MIN_PASSWORD_LENGTH) && atLeastOneDigits() && atLeastOneUppercaseLetter()
}

object ValidationUtil {
    private val githubProfileUrlRegex = "^(https?://)?github.com/([A-Za-z0-9_-]+)/?\$".toRegex()
    private val linkedinProfileUrlRegex =
        "http(s)?://(\\w+\\.)?linkedin\\.com/in/[A-z0-9_-]+/?".toRegex()
    private val instagramProfileUrlRegex =
        "^(http://)?instagram\\.com/[a-z\\d-_]{1,255}\\s*\$".toRegex()

    fun validateEmail(email: String): AuthValidationError? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            return AuthValidationError.FieldEmpty
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail)
                .matches()
        ) return AuthValidationError.InvalidEmail
        return null
    }

    fun validateUsername(username: String): AuthValidationError? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) return AuthValidationError.FieldEmpty
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH) return AuthValidationError.InputTooShort

        return null
    }

    fun validatePassword(password: String): AuthValidationError? {
        if (password.isBlank()) return AuthValidationError.FieldEmpty
        if (password.length < Constants.MIN_PASSWORD_LENGTH) return AuthValidationError.InputTooShort
        val capitalLetterInPassword = password.any { it.isUpperCase() }
        val numberInPassword = password.any { it.isDigit() }
        if (!capitalLetterInPassword || !numberInPassword) {
            return AuthValidationError.InvalidPassword
        }

        return null
    }

    fun validateComment(comment: String): CreateCommentValidationError? {
        if (comment.isBlank()) return CreateCommentValidationError.FieldEmpty
        if (comment.length > Constants.MAX_COMMENT_LENGTH) return CreateCommentValidationError.InputTooLong
        return null
    }

    fun validateIsNullOrEmpty(value: String?): Boolean {
        return value.isNullOrEmpty()
    }

    fun isGithubLinkValid(link: String?): Boolean {
        if (!link.isNullOrEmpty()) return githubProfileUrlRegex.matches(link)

        return true

    }

    fun isLinkedinLinkValid(link: String?): Boolean {
        if (!link.isNullOrEmpty()) return linkedinProfileUrlRegex.matches(link)

        return true
    }

    fun isInstagramLinkValid(link: String?): Boolean {
        if (!link.isNullOrEmpty()) return instagramProfileUrlRegex.matches(link)

        return true
    }
}
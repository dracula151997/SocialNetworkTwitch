package com.dracula.socialnetworktwitch.core.domain.utils

import android.util.Patterns
import com.dracula.socialnetworktwitch.core.utils.Constants
import java.util.regex.Pattern

fun String.isValidEmail(): Boolean {
    return Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), this.trim())
}

fun String.matchesMinLength(minLength: Int): Boolean {
    return this.trim().length >= minLength
}

fun String.matchesMaxLength(maxLength: Int): Boolean {
    return this.trim().length >= maxLength
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

fun String.isValidGithubUrl(): Boolean {
    return "^(https?://)?github.com/([A-Za-z0-9_-]+)/?\$".toRegex().matches(this)
}

fun String.isValidLinkedinUrl(): Boolean {
    return "http(s)?://(\\w+\\.)?linkedin\\.com/in/[A-z0-9_-]+/?".toRegex().matches(this)
}

fun String.isValidInstagramUrl(): Boolean {
    return "^(http://)?instagram\\.com/[a-z\\d-_]{1,255}\\s*\$".toRegex().matches(this)
}
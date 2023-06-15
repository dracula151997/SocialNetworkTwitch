package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.utils.isValidInstagramUrl
import com.dracula.socialnetworktwitch.core.utils.UiText

class InstagramFieldState(
    newText: String? = null,
) : TextFieldState(
    validator = ::isValid,
    errorFor = ::validationErrorMessage,
) {
    init {
        newText?.let {
            text = it
        }
    }
}

private fun validationErrorMessage(text: String): UiText {
    return UiText.StringResource(R.string.invalid_instagram_link)
}

private fun isValid(text: String): Boolean {
    if (text.isNotEmpty())
        return text.isValidInstagramUrl()
    return true
}
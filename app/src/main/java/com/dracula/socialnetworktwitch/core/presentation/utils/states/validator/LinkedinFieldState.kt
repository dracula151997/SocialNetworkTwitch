package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.utils.isValidLinkedinUrl
import com.dracula.socialnetworktwitch.core.utils.UiText

class LinkedinFieldState : TextFieldState(
    validator = ::isValid,
    errorFor = ::validationErrorMessage
)

private fun validationErrorMessage(text: String): UiText {
    return UiText.StringResource(R.string.invalid_linkedin_link)
}

private fun isValid(text: String): Boolean {
    if (text.isNotEmpty())
        return text.isValidLinkedinUrl()
    return true
}
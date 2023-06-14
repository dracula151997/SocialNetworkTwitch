package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.UiText

class NotEmptyFieldState(
    newText: String? = null
) : TextFieldState(
    validator = ::isValid,
    errorFor = { validationErrorMessage() }
) {
    init {
        newText?.let {
            text = it
        }
    }
}

private fun validationErrorMessage(): UiText {
    return UiText.StringResource(R.string.error_this_field_cannot_be_empty)
}

private fun isValid(text: String): Boolean {
    return text.isNotEmpty()
}



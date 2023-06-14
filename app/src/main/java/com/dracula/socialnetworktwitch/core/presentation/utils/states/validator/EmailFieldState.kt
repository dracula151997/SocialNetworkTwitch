package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import android.util.Patterns
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.UiText
import java.util.regex.Pattern

class EmailFieldState(email: String? = null) : TextFieldState(
    validator = ::isValid,
    errorFor = ::validationErrorMessage
) {
    init {
        email?.let {
            text = it
        }
    }
}

private fun validationErrorMessage(email: String): UiText {
    if (email.isEmpty())
        return UiText.StringResource(R.string.error_this_field_cannot_be_empty)

    return UiText.StringResource(R.string.error_not_a_valid_email)
}

private fun isValid(email: String): Boolean {
    return email.isNotEmpty() && Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)
}
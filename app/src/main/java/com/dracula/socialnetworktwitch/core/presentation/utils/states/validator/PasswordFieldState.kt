package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.utils.isValidPassword
import com.dracula.socialnetworktwitch.core.domain.utils.matchesMinLength
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiText

class PasswordFieldState(password: String? = null) : TextFieldState(
    validator = ::isPasswordValid,
    errorFor = ::validationErrorMessage
) {
    init {
        password?.let {
            text = it
        }
    }
}

private fun validationErrorMessage(password: String): UiText {
    return if (password.isEmpty())
        UiText.StringResource(R.string.error_this_field_cannot_be_empty)
    else if (!password.matchesMinLength(minLength = Constants.MIN_PASSWORD_LENGTH))
        UiText.StringResourceWithArgs(R.string.error_input_to_short, Constants.MIN_PASSWORD_LENGTH)
    else
        UiText.StringResource(R.string.error_invalid_password)
}

private fun isPasswordValid(password: String): Boolean {
    return password.isValidPassword()
}

package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.utils.isValidGithubUrl
import com.dracula.socialnetworktwitch.core.utils.UiText

class GithubFieldState(
    newText: String? = null,
) : TextFieldState(
    validator = ::isValid,
    errorFor = ::validationMessageError
) {
    init {
        newText?.let {
            text = it
        }
    }
}

private fun validationMessageError(text: String): UiText {
    return UiText.StringResource(R.string.invalid_github_link)
}

private fun isValid(text: String): Boolean {
    if (text.isNotEmpty())
        return text.isValidGithubUrl()
    return true
}


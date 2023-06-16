package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.utils.matchesMaxLength
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiText

class CommentFieldState(comment: String? = null) : TextFieldState(
    validator = ::isValid,
    errorFor = ::validationErrorMessage
) {
    init {
        comment?.let {
            text = it
        }
    }
}

private fun validationErrorMessage(text: String): UiText {
    if (text.isEmpty())
        return UiText.StringResource(R.string.error_this_field_cannot_be_empty)
    return UiText.StringResourceWithArgs(
        R.string.error_input_too_long,
        Constants.MAX_COMMENT_LENGTH
    )
}

private fun isValid(text: String): Boolean {
    return text.isNotEmpty() && text.matchesMaxLength(Constants.MAX_COMMENT_LENGTH)
}
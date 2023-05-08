package com.dracula.socialnetworktwitch.feature_post.domain.model

import com.dracula.socialnetworktwitch.core.utils.ValidationError

sealed class CreateCommentValidationError : ValidationError() {
    object FieldEmpty : CreateCommentValidationError()
    object InputTooLong : CreateCommentValidationError()

}
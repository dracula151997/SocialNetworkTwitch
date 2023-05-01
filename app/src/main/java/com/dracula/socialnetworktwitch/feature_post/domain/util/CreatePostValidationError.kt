package com.dracula.socialnetworktwitch.feature_post.domain.util

import com.dracula.socialnetworktwitch.core.utils.ValidationError

sealed class CreatePostValidationError : ValidationError() {
    object FieldEmpty : CreatePostValidationError()
    object NoImagePicked : CreatePostValidationError()
}
package com.dracula.socialnetworktwitch.feature_post.domain.util

import com.dracula.socialnetworktwitch.core.utils.Error

sealed class CreatePostError : Error() {
    object FieldEmpty: CreatePostError()
    object NoImagePicked: CreatePostError()
}
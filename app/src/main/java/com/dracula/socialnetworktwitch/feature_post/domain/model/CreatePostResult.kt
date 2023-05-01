package com.dracula.socialnetworktwitch.feature_post.domain.model

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.util.CreatePostValidationError

data class CreatePostResult(
    val descriptionError: CreatePostValidationError? = null,
    val imageError: CreatePostValidationError? = null,
    val result: UnitApiResult? = null
) {
    val hasDescriptionError get() = descriptionError != null
    val hasImageError get() = imageError != null
}
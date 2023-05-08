package com.dracula.socialnetworktwitch.feature_post.domain.model

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult

data class CreateCommentResult(
    val commentError: CreateCommentValidationError? = null,
    val postIdError: CreateCommentValidationError? = null,
    val result: UnitApiResult? = null
) {
    val hasCommentError: Boolean = commentError != null
    val hasPostIdError: Boolean = postIdError != null
}

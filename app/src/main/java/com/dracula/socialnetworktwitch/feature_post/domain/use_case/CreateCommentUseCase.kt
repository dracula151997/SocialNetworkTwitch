package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import com.dracula.socialnetworktwitch.core.domain.utils.ValidationUtil
import com.dracula.socialnetworktwitch.feature_post.domain.model.CreateCommentResult
import com.dracula.socialnetworktwitch.feature_post.domain.model.CreateCommentValidationError
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CreateCommentUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(comment: String, postId: String): CreateCommentResult {
        val trimmedComment = comment.trim()
        val commentError = ValidationUtil.validateComment(trimmedComment)
        val postIdError = if (postId.isBlank()) CreateCommentValidationError.FieldEmpty else null
        if (commentError != null || postIdError != null) {
            return CreateCommentResult(commentError = commentError, postIdError = postIdError)
        }
        val apiResult = repository.createComment(
            trimmedComment, postId
        )
        return CreateCommentResult(
            result = apiResult
        )
    }
}
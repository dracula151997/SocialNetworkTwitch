package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import android.net.Uri
import com.dracula.socialnetworktwitch.feature_post.domain.model.CreatePostResult
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import com.dracula.socialnetworktwitch.feature_post.domain.util.CreatePostValidationError

class CreatePostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(description: String, imageUri: Uri?): CreatePostResult {
        val imageUriError = if (imageUri == null) CreatePostValidationError.NoImagePicked else null

        if (imageUriError != null)
            return CreatePostResult(imageError = imageUriError)
        val result = repository.createPost(description.trim(), imageUri!!)

        return CreatePostResult(result = result)
    }
}
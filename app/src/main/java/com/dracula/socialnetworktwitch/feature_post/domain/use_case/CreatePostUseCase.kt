package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import android.net.Uri
import com.dracula.socialnetworktwitch.feature_post.domain.model.CreatePostResult
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import com.dracula.socialnetworktwitch.feature_post.domain.util.CreatePostError

class CreatePostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(description: String, imageUri: Uri?): CreatePostResult {
        val trimmedDescription = description.trim()
        val descriptionError =
            if (trimmedDescription.isBlank()) CreatePostError.FieldEmpty else null
        val imageUriError = if (imageUri == null) CreatePostError.NoImagePicked else null

        if (descriptionError != null || imageUriError != null)
            return CreatePostResult(descriptionError = descriptionError, imageError = imageUriError)
        val result = repository.createPost(trimmedDescription, imageUri!!)


        return CreatePostResult(result = result)
    }
}
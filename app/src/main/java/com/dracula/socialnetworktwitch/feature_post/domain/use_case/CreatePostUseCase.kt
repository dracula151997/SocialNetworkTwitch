package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import android.net.Uri
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository

class CreatePostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(description: String, imageUri: Uri): UnitApiResult {
        return repository.createPost(description, imageUri)
    }
}
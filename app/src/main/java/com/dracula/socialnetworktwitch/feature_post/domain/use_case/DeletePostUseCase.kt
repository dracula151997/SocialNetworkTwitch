package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeletePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): UnitApiResult {
        return postRepository.deletePost(postId)
    }
}
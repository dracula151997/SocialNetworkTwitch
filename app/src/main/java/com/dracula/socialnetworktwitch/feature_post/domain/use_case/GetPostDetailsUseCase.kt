package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class GetPostDetailsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): ApiResult<Post> {
        return repository.getPostDetails(postId)
    }
}
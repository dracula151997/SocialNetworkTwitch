package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.Post
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository

class GetPostsForFollowsUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): ApiResult<List<Post>> {
        return repository.getPostsForFollows(page, pageSize)
    }
}
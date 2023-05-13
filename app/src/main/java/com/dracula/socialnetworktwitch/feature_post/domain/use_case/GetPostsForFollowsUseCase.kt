package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetPostsForFollowsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return flowOf(PagingData.empty())
    }
}
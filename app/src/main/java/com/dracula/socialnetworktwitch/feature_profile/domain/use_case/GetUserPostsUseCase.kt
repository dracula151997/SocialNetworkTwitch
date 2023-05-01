package com.dracula.socialnetworktwitch.feature_profile.domain.use_case

import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetUserPostsUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(userId: String): Flow<PagingData<Post>> {
        return repository.getUserPosts(
            userId = userId
        )
    }
}
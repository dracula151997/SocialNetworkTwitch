package com.dracula.socialnetworktwitch.feature_profile.domain.use_case

import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository

class GetUserPostsUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String, page: Int, pageSize: Int): ApiResult<List<Post>> {
        return repository.getPostPaged(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }
}
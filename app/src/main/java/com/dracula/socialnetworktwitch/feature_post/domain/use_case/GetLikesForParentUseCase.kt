package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import com.dracula.socialnetworktwitch.core.domain.model.UserItem
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class GetLikesForParentUseCase @Inject constructor(
    val repository: PostRepository
) {
    suspend operator fun invoke(parentId: String): ApiResult<List<UserItem>> {
        return repository.getLikesForParent(parentId)
    }
}
package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class LikeParentUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(parentId: String, parentType: Int): UnitApiResult {
        return repository.likeParent(parentId, parentType)
    }
}
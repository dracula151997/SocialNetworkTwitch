package com.dracula.socialnetworktwitch.feature_post.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ToggleLikeForParentUseCase @Inject constructor(
    private val likeParentUseCase: LikeParentUseCase,
    private val unlikeParentUseCase: UnlikeParentUseCase
) {
    suspend operator fun invoke(
        parentId: String,
        parentType: Int,
        isLiked: Boolean
    ): UnitApiResult {
        return if (isLiked)
            unlikeParentUseCase.invoke(parentId, parentType)
        else likeParentUseCase(parentId, parentType)
    }
}
package com.dracula.socialnetworktwitch.feature_search.domain

import com.dracula.socialnetworktwitch.core.domain.model.UserItem
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository

class SearchUserUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        username: String
    ): ApiResult<List<UserItem>> {
        if (username.isEmpty())
            return ApiResult.Success(data = emptyList())

        return repository.searchUser(username)
    }
}
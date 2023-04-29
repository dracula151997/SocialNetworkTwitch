package com.dracula.socialnetworktwitch.feature_profile.domain.repository

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(userId: String): ApiResult<Profile>
}
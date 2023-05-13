package com.dracula.socialnetworktwitch.feature_profile.domain.repository

import android.net.Uri
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.domain.model.UserItem
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.UpdateProfileRequest
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

interface ProfileRepository {

    suspend fun getProfile(userId: String): ApiResult<Profile>

    suspend fun getSkills(): ApiResult<List<Skill>>

    suspend fun updateProfile(
        updateProfileData: UpdateProfileRequest,
        bannerImageUri: Uri?,
        profilePictureUri: Uri?
    ): UnitApiResult

    suspend fun getPostPaged(
        userId: String,
        page: Int = Constants.DEFAULT_PAGE,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): ApiResult<List<Post>>

    suspend fun searchUser(username: String): ApiResult<List<UserItem>>

    suspend fun followUser(userId: String): UnitApiResult

    suspend fun unfollowUser(userId: String): UnitApiResult
}
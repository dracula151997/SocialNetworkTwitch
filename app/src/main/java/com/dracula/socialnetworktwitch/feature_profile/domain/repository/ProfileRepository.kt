package com.dracula.socialnetworktwitch.feature_profile.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.UpdateProfileRequest
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getProfile(userId: String): ApiResult<Profile>

    suspend fun getSkills(): ApiResult<List<Skill>>

    suspend fun updateProfile(
        updateProfileData: UpdateProfileRequest,
        bannerImageUri: Uri?,
        profilePictureUri: Uri?
    ): UnitApiResult

    fun getUserPosts(userId: String): Flow<PagingData<Post>>
}
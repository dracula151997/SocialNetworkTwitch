package com.dracula.socialnetworktwitch.feature_profile.domain.use_case

import android.net.Uri
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.UpdateProfileRequest
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        updateProfileRequest: UpdateProfileRequest,
        bannerImage: Uri?,
        profilePicture: Uri?
    ): UnitApiResult {
        return repository.updateProfile(
            updateProfileData = updateProfileRequest,
            bannerImageUri = bannerImage,
            profilePictureUri = profilePicture
        )
    }

}
package com.dracula.socialnetworktwitch.feature_profile.domain.use_case

import android.net.Uri
import com.dracula.socialnetworktwitch.core.domain.utils.ValidationUtil
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.UpdateProfileRequest
import com.dracula.socialnetworktwitch.feature_profile.domain.model.EditProfileResult
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository
import com.dracula.socialnetworktwitch.feature_profile.domain.utils.EditProfileValidationError

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        updateProfileRequest: UpdateProfileRequest,
        bannerImage: Uri?,
        profilePicture: Uri?
    ): EditProfileResult {
        val usernameError =
            if (updateProfileRequest.username.isBlank()) EditProfileValidationError.FieldEmpty else null
        val githubError =
            if (!ValidationUtil.isGithubLinkValid(updateProfileRequest.gitHubUrl)) EditProfileValidationError.InvalidLink else null
        val instagramError =
            if (!ValidationUtil.isInstagramLinkValid(updateProfileRequest.instagramUrl)) EditProfileValidationError.InvalidLink else null
        val linkedinError =
            if (!ValidationUtil.isLinkedinLinkValid(updateProfileRequest.linkedInUrl)) EditProfileValidationError.InvalidLink else null

        if (usernameError != null || githubError != null || instagramError != null || linkedinError != null) {
            return EditProfileResult.error(
                usernameError = usernameError,
                githubError = githubError,
                instagramError = instagramError,
                linkedinError = linkedinError
            )
        }
        val result = repository.updateProfile(
            updateProfileData = updateProfileRequest,
            bannerImageUri = bannerImage,
            profilePictureUri = profilePicture
        )
        return EditProfileResult.success(result = result)
    }

}
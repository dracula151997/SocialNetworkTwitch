package com.dracula.socialnetworktwitch.feature_profile.domain.model

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_profile.domain.utils.EditProfileValidationError

data class EditProfileResult(
    val usernameError: EditProfileValidationError? = null,
    val githubError: EditProfileValidationError? = null,
    val linkedinError: EditProfileValidationError? = null,
    val instagramError: EditProfileValidationError? = null,
    val result: UnitApiResult? = null,
) {
    val hasUsernameError get() = usernameError != null
    val hasGithubError get() = githubError != null

    val hasLinkedinError get() = linkedinError != null

    val hasInstagramError get() = instagramError != null

    companion object {
        fun success(result: UnitApiResult?): EditProfileResult {
            return EditProfileResult(result = result)
        }

        fun error(
            usernameError: EditProfileValidationError?,
            githubError: EditProfileValidationError?,
            instagramError: EditProfileValidationError?,
            linkedinError: EditProfileValidationError?
        ): EditProfileResult {
            return EditProfileResult(
                usernameError = usernameError,
                githubError = githubError,
                instagramError = instagramError,
                linkedinError = linkedinError
            )
        }
    }
}

package com.dracula.socialnetworktwitch.feature_profile.domain.model

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_profile.domain.utils.EditProfileValidationError

data class EditProfileResult(
    val usernameError: EditProfileValidationError? = null,
    val githubError: EditProfileValidationError? = null,
    val result: UnitApiResult? = null,
) {
    val hasUsernameError get() = usernameError != null
    val hasGithubError get() = githubError != null

    companion object {
        fun success(result: UnitApiResult?): EditProfileResult {
            return EditProfileResult(result = result)
        }

        fun error(
            usernameError: EditProfileValidationError?,
            githubError: EditProfileValidationError?,
        ): EditProfileResult {
            return EditProfileResult(
                usernameError = usernameError,
                githubError = githubError
            )
        }
    }
}

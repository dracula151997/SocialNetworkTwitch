package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile

data class EditProfileState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
) {
    companion object {
        val EMPTY = EditProfileState()

        fun loading(): EditProfileState {
            return EditProfileState(isLoading = true)
        }

        fun success(profile: Profile?): EditProfileState {
            return EditProfileState(isLoading = false, profile = profile)
        }

        fun error(): EditProfileState {
            return EditProfileState(isLoading = false)
        }
    }
}

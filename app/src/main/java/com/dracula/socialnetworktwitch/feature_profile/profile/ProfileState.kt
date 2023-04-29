package com.dracula.socialnetworktwitch.feature_profile.profile

import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val data: Profile? = null,
) {
    companion object {
        fun loading() = ProfileState(isLoading = true)
        fun success(data: Profile?) = ProfileState(isLoading = false, data = data)
        fun error() = ProfileState(isLoading = false)
        fun idle() = ProfileState(isLoading = false)
    }
}

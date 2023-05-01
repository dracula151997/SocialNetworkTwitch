package com.dracula.socialnetworktwitch.feature_profile.domain.utils

import com.dracula.socialnetworktwitch.core.utils.ValidationError

sealed class EditProfileValidationError : ValidationError() {
    object FieldEmpty : EditProfileValidationError()
    object NoProfileImagePicked : EditProfileValidationError()
    object NoBannerImagePicked : EditProfileValidationError()
    object InvalidLink : EditProfileValidationError()
}
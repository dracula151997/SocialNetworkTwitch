package com.dracula.socialnetworktwitch.feature_profile.utils

import com.dracula.socialnetworktwitch.core.utils.Error

sealed class EditProfileError : Error() {
    object FieldEmpty: EditProfileError()
}

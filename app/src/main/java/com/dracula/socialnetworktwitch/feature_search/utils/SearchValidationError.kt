package com.dracula.socialnetworktwitch.feature_search.utils

import com.dracula.socialnetworktwitch.core.utils.ValidationError

sealed class SearchValidationError : ValidationError() {
    object FieldEmpty : SearchValidationError()
}

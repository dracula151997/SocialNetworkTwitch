package com.dracula.socialnetworktwitch.feature_search.utils

import com.dracula.socialnetworktwitch.core.utils.Error

sealed class SearchError: Error(){
    object FieldEmpty: SearchError()
}

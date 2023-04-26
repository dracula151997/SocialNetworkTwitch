package com.dracula.socialnetworktwitch.core.utils

typealias UnitApiResult = ApiResult<Unit>

sealed class ApiResult<T>(val data: T? = null, val uiText: UiText? = null) {
    class Success<T>(data: T?) : ApiResult<T>(data)
    class Error<T>(message: UiText?, data: T? = null) : ApiResult<T>(data, message)
}

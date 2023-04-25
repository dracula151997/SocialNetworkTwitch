package com.dracula.socialnetworktwitch.core.data.remote.dto.response

typealias UnitApiResponse = BasicApiResponse<Unit>
data class BasicApiResponse<T>(
    val successful: Boolean,
    val message: String? = null,
    val data: T? = null
)

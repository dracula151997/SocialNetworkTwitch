package com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.dto.request

data class CreateAccountRequest(
    val email: String,
    val username: String,
    val password: String
)

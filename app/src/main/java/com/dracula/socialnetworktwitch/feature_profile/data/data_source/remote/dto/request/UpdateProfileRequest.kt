package com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request

import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val gitHubUrl: String,
    val instagramUrl: String,
    val linkedInUrl: String,
    val skills: List<Skill>,
)

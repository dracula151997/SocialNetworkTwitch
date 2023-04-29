package com.dracula.socialnetworktwitch.feature_profile.domain.model

import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response.ProfileResponse
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response.toSkill

data class Profile(
    val userId: String,
    val username: String,
    val bio: String,
    val followerCount: Int,
    val followingCount: Int,
    val postCount: Int,
    val profilePictureUrl: String,
    val bannerUrl: String?,
    val topSkills: List<Skill>,
    val gitHubUrl: String?,
    val instagramUrl: String?,
    val linkedinUrl: String?,
    val isOwnProfile: Boolean,
    val isFollowing: Boolean
) {
    companion object {
        fun empty(): Profile {
            return Profile(
                userId = "",
                username = "",
                bio = "",
                followerCount = 0,
                followingCount = 0,
                postCount = 0,
                profilePictureUrl = "",
                bannerUrl = null,
                topSkills = emptyList(),
                gitHubUrl = null,
                instagramUrl = null,
                linkedinUrl = null,
                isOwnProfile = false,
                isFollowing = false
            )
        }
    }
}

fun ProfileResponse.toProfile(): Profile {
    return Profile(
        userId,
        username,
        bio,
        followerCount,
        followingCount,
        postCount,
        profilePictureUrl,
        bannerUrl,
        topSkills.map { it.toSkill() },
        gitHubUrl,
        instagramUrl,
        linkedInUrl,
        isOwnProfile,
        isFollowing
    )
}
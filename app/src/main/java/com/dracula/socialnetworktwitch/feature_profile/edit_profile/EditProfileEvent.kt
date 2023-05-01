package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

sealed interface EditProfileEvent {
    data class UsernameEntered(val username: String) : EditProfileEvent
    data class GithubUrlEntered(val githubUrl: String) : EditProfileEvent
    data class InstagramUrlEntered(val instagramUrl: String) : EditProfileEvent
    data class LinkedinUrlEntered(val linkedinUrl: String) : EditProfileEvent
    data class BioEntered(val bio: String) : EditProfileEvent
    data class SkillSelected(val skill: Skill, val isSelected: Boolean = false) : EditProfileEvent
    data class CropProfileImage(val uri: Uri?) : EditProfileEvent
    data class CropBannerImage(val uri: Uri?) : EditProfileEvent
    data class GetProfile(val userId: String?) : EditProfileEvent

    object ClearGithubUrlText : EditProfileEvent
    object ClearInstagramUrlText : EditProfileEvent
    object ClearLinkedinUrlText : EditProfileEvent
    object ClearBio : EditProfileEvent
    object GetSkills : EditProfileEvent

    object UpdateProfile : EditProfileEvent
}
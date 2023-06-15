package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

sealed interface EditProfileAction {
    data class BioEntered(val bio: String) : EditProfileAction
    data class SkillSelected(val skill: Skill, val isSelected: Boolean = false) : EditProfileAction
    data class CropProfileImage(val uri: Uri?) : EditProfileAction
    data class CropBannerImage(val uri: Uri?) : EditProfileAction
    data class GetProfile(val userId: String?) : EditProfileAction
    object ClearBio : EditProfileAction
    object GetSkills : EditProfileAction

    object UpdateProfile : EditProfileAction
}
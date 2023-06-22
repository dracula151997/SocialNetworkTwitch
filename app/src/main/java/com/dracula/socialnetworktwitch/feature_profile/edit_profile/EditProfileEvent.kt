package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

sealed class EditProfileEvent : UiEvent() {
    data class SkillSelected(val skill: Skill, val isSelected: Boolean = false) : EditProfileEvent()
    data class CropProfileImage(val uri: Uri?) : EditProfileEvent()
    data class CropBannerImage(val uri: Uri?) : EditProfileEvent()
    data class GetProfile(val userId: String?) : EditProfileEvent()
    object GetSkills : EditProfileEvent()
    object UpdateProfile : EditProfileEvent()
    object Refresh : EditProfileEvent()
}
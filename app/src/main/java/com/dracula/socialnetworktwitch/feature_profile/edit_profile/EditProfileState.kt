package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import com.dracula.socialnetworktwitch.core.presentation.utils.UiState
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

data class EditProfileState(
    val isLoading: Boolean = false,
    val refreshing: Boolean = false,
    val profile: Profile? = null,
    val skillsState: SkillsState = SkillsState()
) : UiState()

data class SkillsState(
    val skills: List<Skill> = emptyList(),
    val selectedSkills: List<Skill> = emptyList(),
)

package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

data class SkillsState(
    val skills: List<Skill> = emptyList(),
    val selectedSkills: List<Skill> = emptyList(),
)
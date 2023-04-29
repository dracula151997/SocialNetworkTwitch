package com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response

import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

data class SkillDto(
    val id: String,
    val name: String,
    val imageUrl: String
)

fun SkillDto.toSkill(): Skill = Skill(name, imageUrl)

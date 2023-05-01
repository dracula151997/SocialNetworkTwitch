package com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response

import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import com.google.gson.annotations.SerializedName

data class SkillDto(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val imageUrl: String
)

fun SkillDto.toSkill(): Skill = Skill(name, imageUrl)

fun List<SkillDto>.toSkillList(): List<Skill> = map { it.toSkill() }

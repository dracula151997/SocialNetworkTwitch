package com.dracula.socialnetworktwitch.feature_profile.domain.repository

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

class GetSkillsUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): ApiResult<List<Skill>> {
        return repository.getSkills()
    }
}
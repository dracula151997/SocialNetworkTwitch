package com.dracula.socialnetworktwitch.feature_profile.domain.use_case

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

class SetSkillSelectedUseCase {
    operator fun invoke(
        selectedSkills: List<Skill>,
        skillToToggle: Skill
    ): ApiResult<List<Skill>> {
        val skillInList = selectedSkills.find {
            it.name == skillToToggle.name
        }
        if (skillInList != null)
            return ApiResult.Success(selectedSkills - skillInList)

        return if (selectedSkills.size >= 3)
            ApiResult.Error(UiText.StringResource(R.string.error_skill_max_skills))
        else ApiResult.Success(selectedSkills + skillToToggle)
    }
}
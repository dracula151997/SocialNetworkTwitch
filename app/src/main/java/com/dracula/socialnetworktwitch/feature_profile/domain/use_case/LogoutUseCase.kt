package com.dracula.socialnetworktwitch.feature_profile.domain.use_case

import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke() {
        profileRepository.logout()
    }
}
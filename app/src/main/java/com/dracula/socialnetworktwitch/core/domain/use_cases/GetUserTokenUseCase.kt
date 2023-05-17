package com.dracula.socialnetworktwitch.core.domain.use_cases

import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetUserTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): String {
        return repository.userToken
    }
}
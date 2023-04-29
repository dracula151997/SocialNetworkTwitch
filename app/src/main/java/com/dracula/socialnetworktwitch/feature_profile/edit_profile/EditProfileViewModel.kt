package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orDefault
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    var state by mutableStateOf(EditProfileState())
        private set

    var usernameState by mutableStateOf(StandardTextFieldState())
        private set

    var githubTextFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var instagramTextFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var linkedInTextFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var bioState by mutableStateOf(StandardTextFieldState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var bannerImageUri: Uri? by mutableStateOf(null)
        private set
    var profileImageUri: Uri? by mutableStateOf(null)
        private set

    var selectedSkills: List<Skill> by mutableStateOf(emptyList())
        private set

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.BioEntered -> bioState = bioState.copy(
                text = event.bio
            )

            is EditProfileEvent.CropBannerImage -> {
                bannerImageUri = event.uri
            }

            is EditProfileEvent.CropProfileImage -> {
                profileImageUri = event.uri
            }

            is EditProfileEvent.GithubUrlEntered -> bioState = bioState.copy(
                text = event.githubUrl
            )

            is EditProfileEvent.InstagramUrlEntered -> bioState = bioState.copy(
                text = event.instagramUrl
            )

            is EditProfileEvent.LinkedinUrlEntered -> bioState = bioState.copy(
                text = event.linkedinUrl
            )

            is EditProfileEvent.SkillSelected -> TODO()
            is EditProfileEvent.UsernameEntered -> bioState = bioState.copy(
                text = event.username
            )

            is EditProfileEvent.GetProfile -> getProfile(event.userId.orDefault(""))
        }
    }

    private fun getProfile(userId: String) {
        viewModelScope.launch {
            state = EditProfileState.loading()
            when (val apiResult = getProfileUseCase(userId)) {
                is ApiResult.Success -> {
                    val profile = apiResult.data ?: kotlin.run {
                        _eventFlow.emit(
                            UiEvent.SnackbarEvent(UiText.StringResource(R.string.error_could_not_load_profile))
                        )
                        return@launch
                    }
                    fillProfileData(profile)
                    state = EditProfileState.success(apiResult.data)

                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(apiResult.uiText.orUnknownError()))
                }
            }
        }
    }

    private fun fillProfileData(profile: Profile) {
        usernameState = usernameState.copy(text = profile.username)
        githubTextFieldState = githubTextFieldState.copy(text = profile.gitHubUrl.orEmpty())
        instagramTextFieldState = instagramTextFieldState.copy(text = profile.instagramUrl.orEmpty())
        linkedInTextFieldState = linkedInTextFieldState.copy(text = profile.linkedinUrl.orEmpty())
        bioState = bioState.copy(text = profile.bio)
        selectedSkills = profile.topSkills
    }
}
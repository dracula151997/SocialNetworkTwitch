package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orDefault
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.UpdateProfileRequest
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.GetSkillsUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.SetSkillSelectedUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getSkillsUseCase: GetSkillsUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val setSkillSelectedUseCase: SetSkillSelectedUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase,
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

    var skillsState: SkillsState by mutableStateOf(SkillsState())
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

            is EditProfileEvent.GithubUrlEntered -> githubTextFieldState =
                githubTextFieldState.copy(
                    text = event.githubUrl
                )

            is EditProfileEvent.InstagramUrlEntered -> instagramTextFieldState =
                instagramTextFieldState.copy(
                    text = event.instagramUrl
                )

            is EditProfileEvent.LinkedinUrlEntered -> linkedInTextFieldState =
                linkedInTextFieldState.copy(
                    text = event.linkedinUrl
                )

            is EditProfileEvent.SkillSelected -> {
                setSkillSelected(skillsState.selectedSkills, event.skill)
            }

            is EditProfileEvent.UsernameEntered -> usernameState = usernameState.copy(
                text = event.username
            )

            is EditProfileEvent.GetProfile -> getProfile(event.userId.orDefault(""))
            EditProfileEvent.GetSkills -> getSkills()
            EditProfileEvent.UpdateProfile -> updateProfile()
            EditProfileEvent.ClearGithubUrlText -> githubTextFieldState =
                githubTextFieldState.copy(text = "")

            EditProfileEvent.ClearBio -> bioState = bioState.copy(text = "")
            EditProfileEvent.ClearInstagramUrlText -> instagramTextFieldState =
                instagramTextFieldState.copy(text = "")

            EditProfileEvent.ClearLinkedinUrlText -> linkedInTextFieldState =
                linkedInTextFieldState.copy(text = "")
        }
    }

    private fun updateProfile() {
        val request = UpdateProfileRequest(
            username = usernameState.text,
            bio = bioState.text,
            gitHubUrl = githubTextFieldState.text,
            instagramUrl = instagramTextFieldState.text,
            linkedInUrl = linkedInTextFieldState.text,
            skills = skillsState.selectedSkills,

            )
        viewModelScope.launch {
            val uiResult = updateProfileUseCase(
                updateProfileRequest = request,
                bannerImage = bannerImageUri,
                profilePicture = profileImageUri
            )
            if (uiResult.hasUsernameError)
                usernameState = usernameState.copy(error = uiResult.usernameError)

            if (uiResult.hasGithubError)
                githubTextFieldState = githubTextFieldState.copy(error = uiResult.githubError)

            if (uiResult.hasInstagramError)
                instagramTextFieldState =
                    instagramTextFieldState.copy(error = uiResult.instagramError)

            if (uiResult.hasLinkedinError)
                linkedInTextFieldState = linkedInTextFieldState.copy(error = uiResult.linkedinError)

            when (val result = uiResult.result) {
                is ApiResult.Success -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.profile_updated_success)))
                    _eventFlow.emit(UiEvent.NavigateUp)
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(result.uiText.orUnknownError()))
                }

                null -> {

                }
            }
        }
    }

    private fun getProfile(userId: String?) {
        viewModelScope.launch {
            state = EditProfileState.loading()
            when (val apiResult = getProfileUseCase(userId ?: getOwnUserIdUseCase())) {
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

    private fun getSkills() {
        viewModelScope.launch {
            when (val result = getSkillsUseCase()) {
                is ApiResult.Success -> {
                    val skills = result.data ?: kotlin.run {
                        _eventFlow.emit(
                            UiEvent.SnackbarEvent(UiText.StringResource(R.string.error_could_not_load_skills))
                        )
                        return@launch
                    }
                    skillsState = skillsState.copy(
                        skills = skills
                    )
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(result.uiText.orUnknownError()))
                    return@launch
                }
            }
        }
    }

    private fun setSkillSelected(selectedSkills: List<Skill>, skill: Skill) {
        viewModelScope.launch {
            when (val result = setSkillSelectedUseCase(selectedSkills, skill)) {
                is ApiResult.Success -> skillsState = skillsState.copy(
                    selectedSkills = result.data ?: kotlin.run {
                        _eventFlow.emit(
                            UiEvent.SnackbarEvent(UiText.unknownError())
                        )
                        return@launch
                    }
                )

                is ApiResult.Error -> _eventFlow.emit(UiEvent.SnackbarEvent(result.uiText.orUnknownError()))
            }
        }
    }

    private fun fillProfileData(profile: Profile) {
        usernameState = usernameState.copy(text = profile.username)
        githubTextFieldState = githubTextFieldState.copy(text = profile.gitHubUrl.orEmpty())
        instagramTextFieldState =
            instagramTextFieldState.copy(text = profile.instagramUrl.orEmpty())
        linkedInTextFieldState = linkedInTextFieldState.copy(text = profile.linkedinUrl.orEmpty())
        bioState = bioState.copy(text = profile.bio)
        skillsState = skillsState.copy(selectedSkills = profile.topSkills)
    }
}
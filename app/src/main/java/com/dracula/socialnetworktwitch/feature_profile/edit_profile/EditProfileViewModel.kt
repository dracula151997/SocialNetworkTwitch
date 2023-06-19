package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.GithubFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.InstagramFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.LinkedinFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
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

    var usernameState by mutableStateOf(NonEmptyFieldState())
        private set

    var githubTextFieldState by mutableStateOf(GithubFieldState())
        private set

    var instagramTextFieldState by mutableStateOf(InstagramFieldState())
        private set

    var linkedInTextFieldState by mutableStateOf(LinkedinFieldState())
        private set

    var bioState by mutableStateOf(TextFieldState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var bannerImageUri: Uri? by mutableStateOf(null)
        private set
    var profileImageUri: Uri? by mutableStateOf(null)
        private set

    var skillsState: SkillsState by mutableStateOf(SkillsState())
        private set

    fun onEvent(event: EditProfileAction) {
        when (event) {

            is EditProfileAction.CropBannerImage -> {
                bannerImageUri = event.uri
            }

            is EditProfileAction.CropProfileImage -> {
                profileImageUri = event.uri
            }


            is EditProfileAction.SkillSelected -> {
                setSkillSelected(skillsState.selectedSkills, event.skill)
            }

            is EditProfileAction.GetProfile -> getProfile(event.userId ?: getOwnUserIdUseCase())
            EditProfileAction.GetSkills -> getSkills()
            EditProfileAction.UpdateProfile -> updateProfile()

            else -> {}
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
            when (uiResult) {
                is ApiResult.Success -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(R.string.profile_updated_success)))
                    _eventFlow.emit(UiEvent.NavigateUp)
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(uiResult.uiText.orUnknownError()))
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
                            UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_could_not_load_profile))
                        )
                        return@launch
                    }
                    fillProfileData(profile)
                    state = EditProfileState.success(apiResult.data)

                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(apiResult.uiText.orUnknownError()))
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
                            UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_could_not_load_skills))
                        )
                        return@launch
                    }
                    skillsState = skillsState.copy(
                        skills = skills
                    )
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.uiText.orUnknownError()))
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
                            UiEvent.ShowSnackbar(UiText.unknownError())
                        )
                        return@launch
                    }
                )

                is ApiResult.Error -> _eventFlow.emit(UiEvent.ShowSnackbar(result.uiText.orUnknownError()))
            }
        }
    }

    private fun fillProfileData(profile: Profile) {
        usernameState.text = profile.username
        githubTextFieldState.text = profile.gitHubUrl.orEmpty()
        instagramTextFieldState.text = profile.instagramUrl.orEmpty()
        linkedInTextFieldState.text = profile.linkedinUrl.orEmpty()
        bioState.text = profile.bio
        skillsState = skillsState.copy(selectedSkills = profile.topSkills)
    }
}
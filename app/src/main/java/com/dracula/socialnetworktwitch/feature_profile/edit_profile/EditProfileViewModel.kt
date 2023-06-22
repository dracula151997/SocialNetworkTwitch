package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.GithubFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.InstagramFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.LinkedinFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.UpdateProfileRequest
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.GetSkillsUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.SetSkillSelectedUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getSkillsUseCase: GetSkillsUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val setSkillSelectedUseCase: SetSkillSelectedUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<EditProfileState, EditProfileEvent>() {

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

    var bannerImageUri: Uri? by mutableStateOf(null)
        private set

    var profileImageUri: Uri? by mutableStateOf(null)
        private set

    init {
        onEvent(EditProfileEvent.GetProfile(savedStateHandle.get<String>(Constants.NavArguments.NAV_USER_ID)))
        onEvent(EditProfileEvent.GetSkills)
    }

    override fun initialState(): EditProfileState {
        return EditProfileState()
    }

    override fun onEvent(event: EditProfileEvent) {
        when (event) {

            is EditProfileEvent.CropBannerImage -> {
                bannerImageUri = event.uri
            }

            is EditProfileEvent.CropProfileImage -> {
                profileImageUri = event.uri
            }


            is EditProfileEvent.SkillSelected -> {
                setSkillSelected(viewState.skillsState.selectedSkills, event.skill)
            }

            is EditProfileEvent.GetProfile -> {
                getProfile(event.userId ?: getOwnUserIdUseCase())
            }

            EditProfileEvent.GetSkills -> getSkills()
            EditProfileEvent.UpdateProfile -> updateProfile()
            EditProfileEvent.Refresh -> getProfile(
                savedStateHandle.get<String>(Constants.NavArguments.NAV_USER_ID)
                    ?: getOwnUserIdUseCase()
            )
        }
    }

    private fun updateProfile() {
        val request = UpdateProfileRequest(
            username = usernameState.text,
            bio = bioState.text,
            gitHubUrl = githubTextFieldState.text,
            instagramUrl = instagramTextFieldState.text,
            linkedInUrl = linkedInTextFieldState.text,
            skills = viewState.skillsState.selectedSkills,

            )
        viewModelScope.launch {
            val uiResult = updateProfileUseCase(
                updateProfileRequest = request,
                bannerImage = bannerImageUri,
                profilePicture = profileImageUri
            )
            when (uiResult) {
                is ApiResult.Success -> {
                    showSnackbar(R.string.profile_updated_success)
                    navigateUp()
                }

                is ApiResult.Error -> {
                    showSnackbar(uiResult.uiText.orUnknownError())
                }
            }
        }
    }

    private fun getProfile(userId: String?, refreshing: Boolean = false) {
        viewModelScope.launch {
            setState { copy(isLoading = !refreshing, refreshing = refreshing) }
            when (val apiResult = getProfileUseCase(userId ?: getOwnUserIdUseCase())) {
                is ApiResult.Success -> {
                    val profile = apiResult.data ?: kotlin.run {
                        showSnackbar(R.string.error_could_not_load_profile)
                        return@launch
                    }
                    fillProfileData(profile)
                    setState {
                        copy(
                            isLoading = false,
                            refreshing = false,
                            profile = profile
                        )
                    }

                }

                is ApiResult.Error -> {
                    setState { copy(isLoading = false, refreshing = false) }
                    showSnackbar(apiResult.uiText.orUnknownError())
                }
            }
        }
    }

    private fun getSkills() {
        viewModelScope.launch {
            when (val result = getSkillsUseCase()) {
                is ApiResult.Success -> {
                    val skills = result.data ?: kotlin.run {
                        showSnackbar(R.string.error_could_not_load_skills)
                        return@launch
                    }
                    setState { copy(skillsState = viewState.skillsState.copy(skills = skills)) }
                }

                is ApiResult.Error -> {
                    showSnackbar(result.uiText.orUnknownError())
                    return@launch
                }
            }
        }
    }

    private fun setSkillSelected(selectedSkills: List<Skill>, skill: Skill) {
        viewModelScope.launch {
            when (val result = setSkillSelectedUseCase(selectedSkills, skill)) {
                is ApiResult.Success -> {
                    setState {
                        copy(
                            skillsState = viewState.skillsState.copy(
                                selectedSkills = result.data ?: emptyList()
                            )
                        )
                    }
                }

                is ApiResult.Error -> {
                    showSnackbar(result.uiText.orUnknownError())
                }
            }
        }
    }

    private fun fillProfileData(profile: Profile) {
        usernameState.text = profile.username
        githubTextFieldState.text = profile.gitHubUrl.orEmpty()
        instagramTextFieldState.text = profile.instagramUrl.orEmpty()
        linkedInTextFieldState.text = profile.linkedinUrl.orEmpty()
        bioState.text = profile.bio
        setState {
            copy(
                skillsState = viewState.skillsState.copy(
                    selectedSkills = profile.topSkills,
                )
            )
        }
    }
}
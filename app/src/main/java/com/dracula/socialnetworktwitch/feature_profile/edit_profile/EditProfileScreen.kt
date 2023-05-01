package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.ClearButton
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.utils.CropActivityResultContract
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import com.dracula.socialnetworktwitch.feature_profile.domain.utils.EditProfileValidationError
import com.dracula.socialnetworktwitch.feature_profile.edit_profile.components.Chip
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun EditProfileScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    userId: String? = null,
    viewModel: EditProfileViewModel = hiltViewModel(),
    profilePictureSize: Dp = ProfilePictureSizeLarge
) {
    val context = LocalContext.current
    val state = viewModel.state
    val profile = state.profile ?: Profile.empty()
    val skillsState = viewModel.skillsState

    val cropProfileImageLauncher =
        rememberLauncherForActivityResult(CropActivityResultContract(2f, 3f)) {
            viewModel.onEvent(EditProfileEvent.CropProfileImage(it))
        }
    val cropBannerImageLauncher =
        rememberLauncherForActivityResult(CropActivityResultContract(16f, 9f)) {
            viewModel.onEvent(EditProfileEvent.CropBannerImage(it))
        }

    val pickProfileImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            cropProfileImageLauncher.launch(it)
        }
    val pickBannerImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            cropBannerImageLauncher.launch(it)
        }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(EditProfileEvent.GetProfile(userId))
        viewModel.onEvent(EditProfileEvent.GetSkills)
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.SnackbarEvent -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(
                        context
                    )
                )

                is UiEvent.NavigateUp -> navController.navigateUp()

                else -> Unit
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StandardTopBar(
            title = stringResource(id = R.string.edit_your_profile), navActions = {
                IconButton(onClick = { viewModel.onEvent(EditProfileEvent.UpdateProfile) }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.save_changes),
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }, showBackButton = true, navController = navController
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BannerEditSection(bannerImageUrl = if (viewModel.bannerImageUri == null) profile.bannerUrl else viewModel.bannerImageUri,
                profileImageUrl = if (viewModel.profileImageUri == null) profile.profilePictureUrl else viewModel.profileImageUri,
                profilePictureSize = profilePictureSize,
                onBannerClick = {
                    pickBannerImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onProfileImageClick = {
                    pickProfileImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                })
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingLarge)
            ) {
                Spacer(modifier = Modifier.height(PaddingMedium))
                StandardTextField(
                    text = viewModel.usernameState.text,
                    hint = stringResource(id = R.string.username),
                    error = when (viewModel.usernameState.error) {
                        EditProfileValidationError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                        else -> ""
                    },
                    leadingIcon = Icons.Default.Person,
                    onValueChanged = {
                        viewModel.onEvent(EditProfileEvent.UsernameEntered(it))
                    },

                    )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.githubTextFieldState.text,
                    hint = stringResource(id = R.string.github_profile_url),
                    error = when (viewModel.githubTextFieldState.error) {
                        EditProfileValidationError.InvalidLink -> stringResource(id = R.string.invalid_github_link)
                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_github_icon_1),
                    onValueChanged = {
                        Timber.d("onValueChanged: GITHUB = $it")
                        viewModel.onEvent(EditProfileEvent.GithubUrlEntered(it))
                    },
                    trailingIcon = {
                        ClearButton {
                            viewModel.onEvent(EditProfileEvent.ClearGithubUrlText)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.instagramTextFieldState.text,
                    hint = stringResource(id = R.string.instagram_profile_url),
                    error = when (viewModel.instagramTextFieldState.error) {
                        EditProfileValidationError.FieldEmpty -> stringResource(
                            id = R.string.error_this_field_cannot_be_empty
                        )

                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_instagram_glyph_1),
                    onValueChanged = {
                        viewModel.onEvent(EditProfileEvent.InstagramUrlEntered(it))

                    },
                    trailingIcon = {
                        ClearButton {
                            viewModel.onEvent(EditProfileEvent.ClearInstagramUrlText)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.linkedInTextFieldState.text,
                    hint = stringResource(id = R.string.linked_in_profile_url),
                    error = when (viewModel.linkedInTextFieldState.error) {
                        EditProfileValidationError.FieldEmpty -> stringResource(
                            id = R.string.error_this_field_cannot_be_empty

                        )

                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_linkedin_icon_1),
                    onValueChanged = {
                        viewModel.onEvent(EditProfileEvent.LinkedinUrlEntered(it))

                    },
                    trailingIcon = {
                        if (viewModel.linkedInTextFieldState.text.isNotEmpty())
                            ClearButton {
                                viewModel.onEvent(EditProfileEvent.ClearLinkedinUrlText)
                            }
                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.bioState.text,
                    hint = stringResource(id = R.string.your_bio),
                    error = when (viewModel.bioState.error) {
                        EditProfileValidationError.FieldEmpty -> stringResource(
                            id = R.string.error_this_field_cannot_be_empty
                        )

                        else -> ""
                    },
                    singleLine = false,
                    maxLines = 3,
                    leadingIcon = Icons.Default.Description,
                    onValueChanged = {
                        viewModel.onEvent(EditProfileEvent.BioEntered(it))

                    },
                    trailingIcon = {
                        ClearButton {
                            viewModel.onEvent(EditProfileEvent.ClearBio)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                Text(
                    text = stringResource(id = R.string.select_top_3_skills),
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(SpaceLarge))
                SkillsFlowRow(skillsState, modifier = Modifier.fillMaxWidth()) {
                    viewModel.onEvent(EditProfileEvent.SkillSelected(skill = it))
                }
            }

        }
    }
}

@Composable
private fun SkillsFlowRow(
    skillsState: SkillsState, modifier: Modifier = Modifier, onSkillClicked: (skill: Skill) -> Unit
) {
    FlowRow(
        modifier = modifier,
        mainAxisAlignment = MainAxisAlignment.Center,
        mainAxisSpacing = SpaceMedium,
        crossAxisSpacing = SpaceMedium
    ) {
        skillsState.skills.forEach { skill ->
            Chip(text = skill.name,
                selected = skillsState.selectedSkills.any { it.name == skill.name },
                onChipClick = { onSkillClicked(skill) })
        }
    }
}

@Composable
fun BannerEditSection(
    bannerImageUrl: Any?,
    profileImageUrl: Any?,
    profilePictureSize: Dp = ProfilePictureSizeLarge,
    onBannerClick: () -> Unit = {},
    onProfileImageClick: () -> Unit = {}
) {
    val bannerHeight = (LocalConfiguration.current.screenWidthDp / 2.5f).dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight + profilePictureSize / 2f)
    ) {
        StandardAsyncImage(
            url = bannerImageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .clickable { onBannerClick() },
        )
        StandardAsyncImage(
            url = profileImageUrl,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(profilePictureSize)
                .clip(CircleShape)
                .border(
                    width = 1.dp, color = MaterialTheme.colors.onSurface, shape = CircleShape
                )
                .clickable { onProfileImageClick() }

        )
    }
}
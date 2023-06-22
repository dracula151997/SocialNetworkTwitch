package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.dracula.socialnetworktwitch.core.presentation.utils.CommonUiEffect
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.CropActivityResultContract
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import com.dracula.socialnetworktwitch.feature_profile.edit_profile.components.Chip
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileRoute(
    userId: String?,
    showSnackbar: (message: String) -> Unit,
    onNavUp: () -> Unit,
) {
    val viewModel: EditProfileViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CommonUiEffect.ShowSnackbar -> showSnackbar(event.uiText.asString(context = context))
                is CommonUiEffect.NavigateUp -> onNavUp()

                else -> Unit
            }
        }

    }
    EditProfileScreen(
        userId = userId,
        onNavUp = onNavUp,
        onEvent = viewModel::onEvent,
        state = viewModel.viewState,
        usernameState = viewModel.usernameState,
        githubTextFieldState = viewModel.githubTextFieldState,
        linkedInTextFieldState = viewModel.linkedInTextFieldState,
        instagramTextFieldState = viewModel.instagramTextFieldState,
        bioTextFieldState = viewModel.bioState,
        bannerImageUri = viewModel.bannerImageUri,
        profileImageUri = viewModel.profileImageUri,
    )
}

@Composable
private fun EditProfileScreen(
    state: EditProfileState,
    usernameState: TextFieldState,
    githubTextFieldState: TextFieldState,
    linkedInTextFieldState: TextFieldState,
    instagramTextFieldState: TextFieldState,
    bioTextFieldState: TextFieldState,
    modifier: Modifier = Modifier,
    userId: String? = null,
    bannerImageUri: Uri? = null,
    profileImageUri: Uri? = null,
    onEvent: (event: EditProfileEvent) -> Unit,
    profilePictureSize: Dp = ProfilePictureSizeLarge,
    onNavUp: () -> Unit
) {
    val profile = state.profile ?: Profile.empty()
    val skillsState = state.skillsState

    val cropProfileImageLauncher =
        rememberLauncherForActivityResult(CropActivityResultContract(1f, 1f)) {
            onEvent(EditProfileEvent.CropProfileImage(it))
        }
    val cropBannerImageLauncher =
        rememberLauncherForActivityResult(CropActivityResultContract(4f, 1f)) {
            onEvent(EditProfileEvent.CropBannerImage(it))
        }

    val pickProfileImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri -> cropProfileImageLauncher.launch(uri) }
        }
    val pickBannerImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri -> cropBannerImageLauncher.launch(uri) }

        }

    LaunchedEffect(key1 = true) {
        onEvent(EditProfileEvent.GetProfile(userId))
        onEvent(EditProfileEvent.GetSkills)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        StandardTopBar(
            title = stringResource(id = R.string.edit_your_profile),
            navActions = {
                IconButton(onClick = { onEvent(EditProfileEvent.UpdateProfile) }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.save_changes),
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            },
            showBackButton = true,
            onBack = onNavUp
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BannerEditSection(
                bannerImageUrl = bannerImageUri ?: profile.bannerUrl,
                profileImageUrl = profileImageUri ?: profile.profilePictureUrl,
                profilePictureSize = profilePictureSize,
                onBannerClick = {
                    pickBannerImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onProfileImageClick = {
                    pickProfileImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingLarge)
            ) {
                Spacer(modifier = Modifier.height(PaddingMedium))
                StandardTextField(
                    state = usernameState,
                    hint = stringResource(id = R.string.username),
                    leadingIcon = Icons.Default.Person,
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    state = githubTextFieldState,
                    hint = stringResource(id = R.string.github_profile_url),
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_github_icon_1),
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = !githubTextFieldState.isEmpty(),
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            ClearButton(
                                onClick = {
                                    githubTextFieldState.clearText()
                                }
                            )
                        }
                    },
                    singleLine = false
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    state = instagramTextFieldState,
                    hint = stringResource(id = R.string.instagram_profile_url),
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_instagram_glyph_1),
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = !instagramTextFieldState.isEmpty(),
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            ClearButton(
                                onClick = { instagramTextFieldState.clearText() },
                            )
                        }

                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    state = linkedInTextFieldState,
                    hint = stringResource(id = R.string.linked_in_profile_url),
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_linkedin_icon_1),
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = !linkedInTextFieldState.isEmpty(),
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            ClearButton(
                                onClick = { linkedInTextFieldState.clearText() }
                            )
                        }

                    },
                    singleLine = false
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    state = bioTextFieldState,
                    hint = stringResource(id = R.string.your_bio),
                    singleLine = false,
                    maxLines = 3,
                    leadingIcon = Icons.Default.Description,
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = !bioTextFieldState.isEmpty(),
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            ClearButton(
                                onClick = { bioTextFieldState.clearText() }
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                Text(
                    text = stringResource(id = R.string.select_top_3_skills),
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(SpaceLarge))
                SkillsFlowRow(
                    skillsState, modifier = Modifier.fillMaxWidth(),
                    onSkillClicked = {
                        onEvent(EditProfileEvent.SkillSelected(skill = it))
                    },
                )
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
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .clickable { onBannerClick() },
            contentDescription = null,

            )
        StandardAsyncImage(
            url = profileImageUrl,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(profilePictureSize)
                .clip(CircleShape)
                .border(
                    width = 1.dp, color = MaterialTheme.colors.onSurface, shape = CircleShape
                )
                .clickable { onProfileImageClick() },
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.avatar_placeholder),
            errorPlaceholder = painterResource(id = R.drawable.avatar_placeholder)

        )
    }
}
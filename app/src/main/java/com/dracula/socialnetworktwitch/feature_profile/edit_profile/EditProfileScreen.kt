package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.feature_profile.edit_profile.components.Chip
import com.dracula.socialnetworktwitch.feature_profile.utils.EditProfileError
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlin.random.Random

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel(),
    profilePictureSize: Dp = ProfilePictureSizeLarge
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StandardTopBar(
            title = stringResource(id = R.string.edit_your_profile),
            navActions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.save_changes),
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            },
            showBackButton = true,
            navController = navController
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BannerEditSection(
                bannerImage = painterResource(id = R.drawable.channelart),
                profileImage = painterResource(id = R.drawable.philipp),
                profilePictureSize = profilePictureSize
            )
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
                        EditProfileError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                        else -> ""
                    },
                    leadingIcon = Icons.Default.Person,
                    onValueChanged = {
                        viewModel.setUsername(
                            StandardTextFieldState(
                                text = it
                            )
                        )
                    },

                    )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.githubTextFieldState.text,
                    hint = stringResource(id = R.string.github_profile_url),
                    error = when (viewModel.githubTextFieldState.error) {
                        EditProfileError.FieldEmpty -> stringResource(
                            id = R.string.error_this_field_cannot_be_empty
                        )

                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_github_icon_1),
                    onValueChanged = {
                        viewModel.setGithub(
                            StandardTextFieldState(text = it)
                        )
                    },
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.instagramTextFieldState.text,
                    hint = stringResource(id = R.string.instagram_profile_url),
                    error = when (viewModel.instagramTextFieldState.error) {
                        EditProfileError.FieldEmpty -> stringResource(
                            id = R.string.error_this_field_cannot_be_empty
                        )

                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_instagram_glyph_1),
                    onValueChanged = {
                        viewModel.setInstagram(
                            StandardTextFieldState(text = it)
                        )
                    },
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.linkedInTextFieldState.text,
                    hint = stringResource(id = R.string.linked_in_profile_url),
                    error = when (viewModel.linkedInTextFieldState.error) {
                        EditProfileError.FieldEmpty -> stringResource(
                            id = R.string.error_this_field_cannot_be_empty

                        )

                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_linkedin_icon_1),
                    onValueChanged = {
                        viewModel.setLinkedIn(
                            StandardTextFieldState(text = it)
                        )
                    },
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.bioState.text,
                    hint = stringResource(id = R.string.your_bio),
                    error = when (viewModel.bioState.error) {
                        EditProfileError.FieldEmpty -> stringResource(
                            id = R.string.error_this_field_cannot_be_empty
                        )
                        else -> ""
                    },
                    singleLine = false,
                    maxLines = 3,
                    leadingIcon = Icons.Default.Description,
                    onValueChanged = {
                        viewModel.setBio(
                            StandardTextFieldState(text = it)
                        )
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
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisAlignment = MainAxisAlignment.Center,
                    mainAxisSpacing = SpaceMedium,
                    crossAxisSpacing = SpaceMedium
                ) {
                    listOf(
                        "Kotlin",
                        "JavaScript",
                        "Assembly",
                        "C++",
                        "C",
                        "C#",
                        "Dart",
                        "TypeScript",
                        "Python",
                    ).forEach {
                        Chip(
                            text = it,
                            selected = Random.nextInt(2) == 0
                        ) {

                        }
                    }
                }
            }

        }
    }
}

@Composable
fun BannerEditSection(
    bannerImage: Painter,
    profileImage: Painter,
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
        Image(
            painter = bannerImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
        )
        Image(
            painter = profileImage,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(profilePictureSize)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface,
                    shape = CircleShape
                )

        )
    }
}
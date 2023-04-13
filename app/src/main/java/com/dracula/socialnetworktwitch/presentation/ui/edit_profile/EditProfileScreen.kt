package com.dracula.socialnetworktwitch.presentation.ui.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.dracula.socialnetworktwitch.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.presentation.ui.edit_profile.components.Chip
import com.dracula.socialnetworktwitch.presentation.ui.theme.*
import com.dracula.socialnetworktwitch.presentation.ui.utils.states.StandardTextFieldState
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
                    text = viewModel.usernameState.value.text,
                    hint = stringResource(id = R.string.username),
                    error = viewModel.usernameState.value.error,
                    leadingIcon = Icons.Default.Person,
                    onValueChanged = {
                        viewModel.setUsernameState(
                            StandardTextFieldState(
                                text = it
                            )
                        )
                    },

                    )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.githubTextFieldState.value.text,
                    hint = stringResource(id = R.string.github_profile_url),
                    error = viewModel.githubTextFieldState.value.error,
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_github_icon_1),
                    onValueChanged = {
                        viewModel.setGithubTextFieldState(
                            StandardTextFieldState(text = it)
                        )
                    },
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.instagramTextFieldState.value.text,
                    hint = stringResource(id = R.string.instagram_profile_url),
                    error = viewModel.instagramTextFieldState.value.error,
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_instagram_glyph_1),
                    onValueChanged = {
                        viewModel.setInstagramTextFieldState(
                            StandardTextFieldState(text = it)
                        )
                    },
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.linkedInTextFieldState.value.text,
                    hint = stringResource(id = R.string.linked_in_profile_url),
                    error = viewModel.linkedInTextFieldState.value.error,
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_linkedin_icon_1),
                    onValueChanged = {
                        viewModel.setLinkedInTextFieldState(
                            StandardTextFieldState(text = it)
                        )
                    },
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.bioState.value.text,
                    hint = stringResource(id = R.string.your_bio),
                    error = viewModel.bioState.value.error,
                    singleLine = false,
                    maxLines = 3,
                    leadingIcon = Icons.Default.Description,
                    onValueChanged = {
                        viewModel.setBioState(
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
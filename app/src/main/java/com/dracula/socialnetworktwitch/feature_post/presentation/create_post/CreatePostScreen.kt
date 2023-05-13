package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall
import com.dracula.socialnetworktwitch.core.utils.CropActivityResultContract
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_post.domain.util.CreatePostValidationError
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreatePostScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val imageUri = viewModel.chosenImageUri
    val descriptionState = viewModel.description
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val cropActivityLauncher = rememberLauncherForActivityResult(
        contract = CropActivityResultContract(16f, 9f),
    ) {
        viewModel.onEvent(CreatePostAction.CropImage(it))
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) {
        cropActivityLauncher.launch(it)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(
                        context
                    )
                )

                UiEvent.NavigateUp -> navController.navigateUp()
                else -> Unit
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            StandardTopBar(
                navController = navController,
                showBackButton = true,
                title = stringResource(id = R.string.create_a_new_post),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingLarge)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onBackground,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.choose_image),
                        tint = MaterialTheme.colors.onBackground
                    )
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uri)
                                    .build()
                            ),
                            contentDescription = Semantics.ContentDescriptions.POST_PHOTO,
                            modifier = Modifier
                                .matchParentSize()
                        )
                    }

                }
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = viewModel.description.text,
                    hint = stringResource(id = R.string.description),
                    singleLine = false,
                    error = when (descriptionState.error) {
                        is CreatePostValidationError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                        else -> ""
                    },
                    maxLines = 5,
                    onValueChanged = {
                        viewModel.onEvent(CreatePostAction.DescriptionEntered(it))
                    },
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onEvent(CreatePostAction.CreatePost)
                            focusManager.clearFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(SpaceLarge))
                Button(
                    onClick = {
                        viewModel.onEvent(CreatePostAction.CreatePost)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = stringResource(id = R.string.post),
                        color = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.width(SpaceSmall))
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                }
                if (state.isLoading)
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
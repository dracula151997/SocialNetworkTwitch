package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthValidationError
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navController: NavController,
    scaffoldState: ScaffoldState,
) {
    val usernameState = viewModel.usernameState
    val emailState = viewModel.emailState
    val passwordState = viewModel.passwordState
    val (isLoading, successful, message) = viewModel.registerState
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.SnackbarEvent ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )

                else -> {}
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = PaddingMedium,
                end = PaddingMedium,
                top = PaddingLarge,
                bottom = 50.dp
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = PaddingMedium),
        ) {
            Text(
                text = stringResource(id = R.string.register),
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = emailState.text,
                onValueChanged = { viewModel.onEvent(RegisterEvent.OnEmailEntered(it)) },
                hint = stringResource(
                    id = R.string.email_hint
                ),
                keyboardType = KeyboardType.Email,
                error = when (emailState.error) {
                    AuthValidationError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                    AuthValidationError.InvalidEmail -> stringResource(id = R.string.error_not_a_valid_email)
                    else -> ""
                },
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = usernameState.text,
                onValueChanged = { viewModel.onEvent(RegisterEvent.OnUserNameEntered(it)) },
                hint = stringResource(
                    id = R.string.username_hint
                ),
                error = when (usernameState.error) {
                    AuthValidationError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                    AuthValidationError.InputTooShort -> stringResource(
                        id = R.string.input_to_short,
                        Constants.MIN_USERNAME_LENGTH
                    )

                    else -> ""
                }
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = passwordState.text,
                onValueChanged = { viewModel.onEvent(RegisterEvent.OnPasswordEntered(it)) },
                keyboardType = KeyboardType.Password,
                hint = stringResource(id = R.string.password_hint),
                error = when (passwordState.error) {
                    AuthValidationError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                    AuthValidationError.InputTooShort -> stringResource(
                        id = R.string.input_to_short,
                        Constants.MIN_PASSWORD_LENGTH,
                    )

                    AuthValidationError.InvalidPassword -> stringResource(id = R.string.error_invalid_password)
                    else -> ""
                },
                showPasswordToggle = passwordState.isPasswordToggleVisible,
                onPasswordToggleClicked = {
                    viewModel.onEvent(RegisterEvent.TogglePasswordVisibility)
                },
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.onEvent(RegisterEvent.Register) },
                ),
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            Button(
                onClick = { viewModel.onEvent(RegisterEvent.Register) },
                modifier = Modifier.align(Alignment.End),
                enabled = !isLoading
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colors.onPrimary
                )
            }

            if (isLoading)
                CircularProgressIndicator()

        }
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.already_have_an_account))
                append(" ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    append(stringResource(id = R.string.sign_in))
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clickable { navController.popBackStack() }
        )


    }

}
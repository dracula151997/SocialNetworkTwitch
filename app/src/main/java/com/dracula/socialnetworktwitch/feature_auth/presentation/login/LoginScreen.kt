package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthValidationError
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun LoginScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val emailState = viewModel.emailState
    val passwordState = viewModel.passwordState
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val signUpText = stringResource(id = R.string.sign_up)
    val signUpAnnotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.dont_have_an_account_yet))
        append(" ")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary
            )
        ) {
            pushStringAnnotation(tag = signUpText, annotation = signUpText)
            append(stringResource(id = R.string.sign_up))
        }

    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> navController.navigate(event.route)
                is UiEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    event.uiText.asString(context), duration = SnackbarDuration.Long
                )

                is UiEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = PaddingMedium, end = PaddingMedium, top = PaddingLarge, bottom = 50.dp
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = PaddingMedium),
        ) {
            Text(
                text = stringResource(id = R.string.login), style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = emailState.text, onValueChanged = {
                    viewModel.onEvent(LoginAction.EmailEntered(it))
                }, hint = stringResource(
                    id = R.string.username_or_email_hint
                ), error = when (emailState.error) {
                    is AuthValidationError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                    else -> ""
                }, keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(text = passwordState.text,
                onValueChanged = {
                    viewModel.onEvent(LoginAction.PasswordEntered(it))
                },
                keyboardType = KeyboardType.Password,
                hint = stringResource(id = R.string.password_hint),
                showPasswordToggle = passwordState.isPasswordToggleVisible,
                onPasswordToggleClicked = {
                    viewModel.onEvent(LoginAction.TogglePasswordVisibility)
                },
                error = when (passwordState.error) {
                    is AuthValidationError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                    else -> ""
                },
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginAction.Login)
                })
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginAction.Login)
                }, modifier = Modifier.align(Alignment.End), enabled = !state.isLoading
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    color = MaterialTheme.colors.onPrimary
                )
            }
            if (state.isLoading) CircularProgressIndicator()
        }
        ClickableText(text = signUpAnnotatedString,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            style = MaterialTheme.typography.body1.copy(
                textAlign = TextAlign.Center
            ),
            onClick = { offset ->
                signUpAnnotatedString.getStringAnnotations(offset, offset).firstOrNull()
                    ?.let { span ->
                        Timber.d("Clicked on: ${span.item}")
                        navController.navigate(Screens.RegisterScreen.route)
                    }
            })


    }

}
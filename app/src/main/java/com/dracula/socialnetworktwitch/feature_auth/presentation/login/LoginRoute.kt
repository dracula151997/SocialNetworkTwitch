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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
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
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.PasswordTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardButton
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.CommonUiEffect
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    onNavigate: (route: String) -> Unit,
    showSnackbar: (message: String) -> Unit,
    onNavigateUp: () -> Unit

) {
    val viewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CommonUiEffect.Navigate -> onNavigate(event.route)
                is CommonUiEffect.ShowSnackbar -> showSnackbar(event.uiText.asString(context))
                is CommonUiEffect.NavigateUp -> onNavigateUp()
            }
        }
    }

    LoginScreen(
        onNavigate = onNavigate,
        state = viewModel.viewState,
        emailFieldState = viewModel.emailState,
        enableLoginButton = viewModel.enableLoginButton,
        onEvent = viewModel::onEvent,
        passwordFieldState = viewModel.passwordState

    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    emailFieldState: TextFieldState,
    passwordFieldState: TextFieldState,
    enableLoginButton: Boolean,
    onEvent: (event: LoginEvent) -> Unit,
    onNavigate: (route: String) -> Unit,
) {
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
                text = stringResource(id = R.string.login), style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                state = emailFieldState,
                hint = stringResource(
                    id = R.string.username_or_email_hint
                ),
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            PasswordTextField(
                state = passwordFieldState,
                hint = stringResource(id = R.string.password_hint),
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onEvent(LoginEvent.Login)
                    },
                )
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardButton(
                text = stringResource(id = R.string.login),
                onClick = {
                    focusManager.clearFocus()
                    onEvent(LoginEvent.Login)
                },
                modifier = Modifier.align(Alignment.End),
                enabled = enableLoginButton
            )
            if (state.isLoading) CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
                    ?.let { _ ->
                        onNavigate(Screens.RegisterScreen.route)
                    }
            })


    }

}
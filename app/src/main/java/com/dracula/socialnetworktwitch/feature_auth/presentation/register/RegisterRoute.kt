package com.dracula.socialnetworktwitch.feature_auth.presentation.register

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
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.PasswordTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.CommonUiEffect
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterRoute(
    showSnackbar: (message: String) -> Unit,
    onNavUp: () -> Unit,
    onNavigate: (route: String) -> Unit
) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CommonUiEffect.ShowSnackbar -> showSnackbar(event.uiText.asString(context = context))
                is CommonUiEffect.Navigate -> onNavigate(event.route)
                CommonUiEffect.NavigateUp -> onNavUp()
            }

        }
    }

    RegisterScreen(
        state = viewModel.viewState,
        onNavUp = onNavUp,
        emailFieldState = viewModel.emailState,
        passwordFieldState = viewModel.passwordState,
        usernameFieldState = viewModel.usernameState,
        enableRegisterButton = viewModel.enableRegisterButton,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    emailFieldState: TextFieldState,
    usernameFieldState: TextFieldState,
    passwordFieldState: TextFieldState,
    enableRegisterButton: Boolean,
    onEvent: (event: RegisterEvent) -> Unit,
    onNavUp: () -> Unit
) {
    val signInText = stringResource(id = R.string.sign_in)
    val annotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.already_have_an_account))
        append(" ")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary
            )
        ) {
            pushStringAnnotation(tag = signInText, annotation = signInText)
            append(stringResource(id = R.string.sign_in))
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
                state = emailFieldState,
                hint = stringResource(
                    id = R.string.email_hint
                ),
                keyboardType = KeyboardType.Email,
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                state = usernameFieldState,
                hint = stringResource(
                    id = R.string.username_hint
                ),
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            PasswordTextField(
                state = passwordFieldState,
                hint = stringResource(id = R.string.password_hint),
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = { onEvent(RegisterEvent.Register) },
                ),
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            Button(
                onClick = { onEvent(RegisterEvent.Register) },
                modifier = Modifier.align(Alignment.End),
                enabled = enableRegisterButton
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colors.onPrimary
                )
            }

            if (state.isLoading) CircularProgressIndicator()

        }
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.let {
                    onNavUp()
                }

            },
            style = MaterialTheme.typography.body1.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )

    }

}
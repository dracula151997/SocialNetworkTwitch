package com.dracula.socialnetworktwitch.presentation.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.presentation.ui.theme.PaddingLarge
import com.dracula.socialnetworktwitch.presentation.ui.theme.PaddingMedium
import com.dracula.socialnetworktwitch.presentation.ui.theme.SpaceSmall

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
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
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
            StandardTextField(
                text = viewModel.usernameText,
                onValueChanged = viewModel::setUsername,
                hint = stringResource(
                    id = R.string.username_hint
                )
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
            StandardTextField(
                text = viewModel.passwordText,
                onValueChanged = viewModel::setPassword,
                keyboardType = KeyboardType.Password,
                hint = stringResource(id = R.string.password_hint)
            )
        }
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.dont_have_an_account_yet))
                append(" ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    append(stringResource(id = R.string.sign_up))
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )


    }

}
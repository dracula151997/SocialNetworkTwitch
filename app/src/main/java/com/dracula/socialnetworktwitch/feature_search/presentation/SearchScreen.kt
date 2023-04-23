package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.components.UserProfileItem
import com.dracula.socialnetworktwitch.core.presentation.theme.IconSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.feature_profile.domain.User
import com.dracula.socialnetworktwitch.feature_search.utils.SearchError

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StandardTopBar(
            title = stringResource(id = R.string.search_for_users),
            showBackButton = true,
            navController = navController
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingLarge)
        ) {
            StandardTextField(
                text = viewModel.searchState.text,
                hint = stringResource(id = R.string.search),
                error = when (viewModel.searchState.error) {
                    SearchError.FieldEmpty -> stringResource(id = R.string.error_this_field_cannot_be_empty)
                    else -> ""
                },
                leadingIcon = Icons.Default.Search,
                onValueChanged = {
                    viewModel.setSearch(
                        StandardTextFieldState(text = it)
                    )
                }
            )
            Spacer(modifier = Modifier.height(SpaceLarge))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(10) {
                    UserProfileItem(
                        user = User(
                            profilePictureUrl = "",
                            username = "Philipp Lackner",
                            description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed\n" +
                                    "diam nonumy eirmod tempor invidunt ut labore et dolore \n" +
                                    "magna aliquyam erat, sed diam voluptua",
                            followerCount = 234,
                            followingCount = 534,
                            postCount = 65
                        ),
                        actionIcon = {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground,
                                modifier = Modifier.size(IconSizeMedium)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(SpaceMedium))
                }
            }
        }
    }
}
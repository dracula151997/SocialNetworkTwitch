package com.dracula.socialnetworktwitch.feature_activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.feature_activity.domain.Activity
import com.dracula.socialnetworktwitch.feature_activity.domain.ActivityAction
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.StandardElevation

@Composable
fun ActivityScreen(
    navController: NavController,
    viewModel: ActivityViewModel = hiltViewModel(),
) {
    Column(modifier = Modifier) {
        StandardTopBar(
            title = stringResource(id = R.string.your_activity),
            navController = navController
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = PaddingMedium, vertical = PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(SpaceMedium)
        ) {
            items(20) {
                ActivityItem(
                    activity = Activity.dummy()
                )
            }
        }
    }

}

@Composable
fun ActivityItem(
    activity: Activity,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = StandardElevation,
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val activityText = when (activity.actionType) {
                ActivityAction.CommentOnPost -> stringResource(
                    id = R.string.commented_on
                )

                ActivityAction.LikedPost -> stringResource(
                    id = R.string.liked,
                )

                ActivityAction.FollowedYou -> stringResource(id = R.string.followed_you)
            }
            Text(
                text = buildAnnotatedString {
                    val boldStyle = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    )
                    withStyle(boldStyle) {
                        append(activity.username)
                    }
                    append(" $activityText ")
                    withStyle(boldStyle) {
                        append(
                            if (!activity.isFollowedYouActionType)
                                stringResource(
                                    id = R.string.on_your_post,
                                ) else "",
                        )
                    }
                },
                style = MaterialTheme.typography.body2
            )
            Text(
                text = activity.timestamp,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2

            )
        }
    }

}
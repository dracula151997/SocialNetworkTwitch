package com.dracula.socialnetworktwitch.feature_activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.PullToRefreshBox
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.StandardElevation
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily
import com.dracula.socialnetworktwitch.core.presentation.utils.CommonUiEffect
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.ActivityType
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.ErrorState
import com.dracula.socialnetworktwitch.core.utils.LoadingState
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun ActivityRoute(
    onNavigate: (route: String) -> Unit,
    showSnackbar: (message: String) -> Unit,
) {
    val viewModel: ActivityViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CommonUiEffect.Navigate -> onNavigate(event.route)
                is CommonUiEffect.ShowSnackbar -> showSnackbar(event.uiText.asString(context = context))
                else -> Unit
            }
        }
    }

    ActivityScreen(
        onNavigate = onNavigate,
        activitiesPagingState = viewModel.viewState,
        onEvent = { viewModel.onEvent(it) },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ActivityScreen(
    activitiesPagingState: PagingState<Activity>,
    modifier: Modifier = Modifier,
    onEvent: (event: ActivityAction) -> Unit,
    onNavigate: (route: String) -> Unit,
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = activitiesPagingState.refreshing,
        onRefresh = { onEvent(ActivityAction.Refreshing) })
    val activities = activitiesPagingState.items
    Column(modifier = modifier) {
        StandardTopBar(
            title = stringResource(id = R.string.your_activity),
        )
        PullToRefreshBox(
            state = pullRefreshState,
            refreshing = activitiesPagingState.refreshing
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = PaddingMedium,
                    vertical = PaddingMedium
                ),
                verticalArrangement = Arrangement.spacedBy(SpaceMedium)
            ) {
                when {
                    activitiesPagingState.isLoading -> item {
                        LoadingState(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center,
                        )
                    }

                    activities.isEmpty() -> item {
                        ErrorState(
                            errorMessage = stringResource(id = R.string.msg_no_activities_right_now),
                            contentAlignment = Alignment.Center,
                            textStyle = MaterialTheme.typography.h6.copy(
                                fontFamily = appFontFamily
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }

                    else -> items(
                        activities,
                        key = { it.parentId }) { activity ->
                        ActivityItem(activity = activity, onNavigate = onNavigate)
                    }

                }

            }
        }
    }

}

@Composable
fun ActivityItem(
    activity: Activity,
    modifier: Modifier = Modifier,
    onNavigate: (route: String) -> Unit
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
                ActivityType.LikedPost -> stringResource(
                    id = R.string.liked,
                )

                ActivityType.CommentedOnPost -> stringResource(
                    id = R.string.commented_on
                )

                ActivityType.FollowedUser -> stringResource(id = R.string.followed_you)
                ActivityType.LikedComment -> stringResource(
                    id = R.string.liked,
                )
            }
            val actionText = when (activity.actionType) {
                ActivityType.CommentedOnPost -> stringResource(R.string.on_your_post)
                ActivityType.FollowedUser -> ""
                ActivityType.LikedComment -> stringResource(R.string.on_your_comment)
                ActivityType.LikedPost -> stringResource(R.string.on_your_post)
            }

            val activityAnnotatedString = buildAnnotatedString {
                val boldStyle = SpanStyle(
                    fontWeight = FontWeight.Bold,
                )
                withStyle(boldStyle) {
                    pushStringAnnotation(
                        tag = Constants.AnnotatedStringTags.ANNOTATION_TAG_USERNAME,
                        annotation = Constants.AnnotatedStringTags.ANNOTATION_TAG_USERNAME
                    )
                    append(activity.username)
                }
                append(" $activityText ")
                withStyle(boldStyle) {
                    pushStringAnnotation(
                        tag = Constants.AnnotatedStringTags.ANNOTATION_TAG_PARENT_ID,
                        annotation = Constants.AnnotatedStringTags.ANNOTATION_TAG_PARENT_ID
                    )
                    append(actionText)
                }
            }
            ClickableText(
                text = activityAnnotatedString,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 12.sp,
                ),
                onClick = { offset ->
                    activityAnnotatedString.getStringAnnotations(offset, offset).firstOrNull()
                        ?.let { annotation ->
                            Timber.d(annotation.tag)
                            when (annotation.tag) {
                                Constants.AnnotatedStringTags.ANNOTATION_TAG_USERNAME -> onNavigate(
                                    Screens.ProfileScreen.createRoute(userId = activity.userId),
                                )

                                Constants.AnnotatedStringTags.ANNOTATION_TAG_PARENT_ID -> onNavigate(
                                    Screens.PostDetailsScreen.createRoute(postId = activity.parentId),
                                )
                            }

                        }
                },
            )
            Text(
                text = activity.formattedTime,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2

            )
        }
    }

}
package com.dracula.socialnetworktwitch.feature_main_feed

import androidx.lifecycle.ViewModel
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetPostsForFollowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val getPostsForFollowsUseCase: GetPostsForFollowsUseCase
) : ViewModel() {


}
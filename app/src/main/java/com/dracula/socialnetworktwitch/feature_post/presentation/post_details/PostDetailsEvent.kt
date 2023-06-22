package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class PostDetailsEvent : UiEvent() {
    object LikePost : PostDetailsEvent()
    object Comment : PostDetailsEvent()
    data class LikeComment(val commentId: String) : PostDetailsEvent()
    object SharePost : PostDetailsEvent()

}
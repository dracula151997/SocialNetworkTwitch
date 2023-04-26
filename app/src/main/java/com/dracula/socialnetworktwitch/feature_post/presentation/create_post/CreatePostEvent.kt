package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import android.net.Uri

sealed interface CreatePostEvent {
    data class DescriptionEntered(val text: String) : CreatePostEvent
    data class PickImage(val uri: Uri?) : CreatePostEvent
    object CreatePost: CreatePostEvent
}
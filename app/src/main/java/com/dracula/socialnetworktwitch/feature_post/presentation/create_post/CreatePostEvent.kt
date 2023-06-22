package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import android.net.Uri
import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent

sealed class CreatePostEvent : UiEvent() {
    data class PickImage(val uri: Uri?) : CreatePostEvent()
    data class CropImage(val uri: Uri?) : CreatePostEvent()
    object CreatePost : CreatePostEvent()

}
package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import android.net.Uri

sealed interface CreatePostAction {
    data class DescriptionEntered(val text: String) : CreatePostAction
    data class PickImage(val uri: Uri?) : CreatePostAction
    data class CropImage(val uri: Uri?) : CreatePostAction
    object CreatePost : CreatePostAction

}
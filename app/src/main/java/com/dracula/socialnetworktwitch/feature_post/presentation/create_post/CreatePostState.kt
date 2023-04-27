package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

data class CreatePostState(
    val isLoading: Boolean = false,

    ) {
    companion object {
        fun loading(): CreatePostState {
            return CreatePostState(isLoading = true)
        }

        fun success(): CreatePostState {
            return CreatePostState(isLoading = false)
        }

        fun error(): CreatePostState {
            return CreatePostState(isLoading = false)
        }

        fun idle(): CreatePostState {
            return CreatePostState(isLoading = false)
        }
    }
}
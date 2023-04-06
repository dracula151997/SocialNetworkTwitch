package com.dracula.socialnetworktwitch.domain.model

data class Post(
    val username: String,
    val imageUrl: String,
    val profileImageUrl: String,
    val description: String,
    val likeCount: Int,
    val commentCount: Int,
    val comment: Comment,
) {
    companion object {
        fun dummy(): Post {
            return Post(
                username = "Amie Bright",
                imageUrl = "https://duckduckgo.com/?q=omnesque",
                profileImageUrl = "https://www.google.com/#q=reprimique",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                likeCount = 2,
                commentCount = 0,
                comment = Comment.dummy()
            )
        }
    }
}
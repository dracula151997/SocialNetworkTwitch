package com.dracula.socialnetworktwitch.feature_profile.domain

data class User(
    val profilePictureUrl: String,
    val username: String,
    val description: String,
    val followingCount: Int,
    val followerCount: Int,
    val postCount: Int,
) {
    companion object {
        fun dummy(): User {
            return User(
                profilePictureUrl = "https://duckduckgo.com/?q=torquent",
                username = "Eva Huber",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eleifend quam adipiscing vitae proin. Nunc sed id semper risus in hendrerit gravida.",
                followingCount = 40,
                followerCount = 3,
                postCount = 10
            )
        }
    }
}
package com.dracula.socialnetworktwitch.core.domain.model

data class User(
    val userId: String,
    val profilePictureUrl: String,
    val username: String,
    val bio: String,
    val followingCount: Int,
    val followerCount: Int,
    val postCount: Int,
) {
    companion object {
        fun dummy(): User {
            return User(
                userId = "6447491868ca6478aaf6bb17",
                profilePictureUrl = "https://duckduckgo.com/?q=torquent",
                username = "Eva Huber",
                bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eleifend quam adipiscing vitae proin. Nunc sed id semper risus in hendrerit gravida.",
                followingCount = 40,
                followerCount = 3,
                postCount = 10,
            )
        }
    }
}
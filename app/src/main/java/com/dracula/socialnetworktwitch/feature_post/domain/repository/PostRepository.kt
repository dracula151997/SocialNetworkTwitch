package com.dracula.socialnetworktwitch.feature_post.domain.repository

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_post.domain.Post

interface PostRepository {
    suspend fun getPostsForFollows(
        page: Int = Constants.DEFAULT_PAGE,
        pageSize: Int = Constants.PAGE_SIZE_POSTS
    ): ApiResult<List<Post>>
}
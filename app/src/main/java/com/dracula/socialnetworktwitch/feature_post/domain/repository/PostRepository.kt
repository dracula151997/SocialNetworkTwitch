package com.dracula.socialnetworktwitch.feature_post.domain.repository

import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.feature_post.domain.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val posts: Flow<PagingData<Post>>
}
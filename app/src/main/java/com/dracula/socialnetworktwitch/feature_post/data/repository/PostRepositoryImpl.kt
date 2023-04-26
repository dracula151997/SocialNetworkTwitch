package com.dracula.socialnetworktwitch.feature_post.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_post.data.data_source.paging.PostSource
import com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.PostApi
import com.dracula.socialnetworktwitch.feature_post.domain.Post
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class PostRepositoryImpl(
    private val api: PostApi
) : PostRepository {

    override val posts: Flow<PagingData<Post>>
        get() = Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE_POSTS
            )
        ) {
            PostSource(api)
        }.flow
}
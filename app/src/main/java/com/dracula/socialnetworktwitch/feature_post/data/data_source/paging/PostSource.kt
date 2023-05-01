package com.dracula.socialnetworktwitch.feature_post.data.data_source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dracula.socialnetworktwitch.core.data.remote.PostApi
import com.dracula.socialnetworktwitch.core.domain.model.Post
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PostSource @Inject constructor(
    private val api: PostApi,
    private val source: Source
) : PagingSource<Int, Post>() {
    private var currentPage = 0
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val nextPage = params.key ?: currentPage
            val posts = when (source) {
                Source.Follows -> api.getPostsForFollows(
                    page = nextPage,
                    pageSize = params.loadSize
                )

                is Source.Profile -> api.getUserPosts(
                    page = nextPage,
                    pageSize = params.loadSize,
                    userId = source.userId
                )
            }
            LoadResult.Page(
                data = posts,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (posts.isEmpty()) null else currentPage + 1
            ).also { currentPage++ }

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    sealed interface Source {
        object Follows : Source
        data class Profile(val userId: String) : Source
    }
}
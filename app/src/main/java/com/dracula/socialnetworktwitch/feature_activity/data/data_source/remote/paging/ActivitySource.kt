package com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.ActivityApi
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import okio.IOException
import retrofit2.HttpException

class ActivitySource(
    private val api: ActivityApi
) : PagingSource<Int, Activity>() {
    private var currentPage: Int = 0
    override fun getRefreshKey(state: PagingState<Int, Activity>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Activity> {
        return try {
            val nextPage = params.key ?: currentPage
            val response = api.getActivityList(
                page = nextPage,
                pageSize = params.loadSize
            )
            LoadResult.Page<Int, Activity>(
                data = response.map { it.toActivity() },
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (response.isEmpty()) null else currentPage + 1
            ).also { currentPage++ }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
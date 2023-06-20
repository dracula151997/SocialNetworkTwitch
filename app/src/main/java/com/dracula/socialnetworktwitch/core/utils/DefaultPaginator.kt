package com.dracula.socialnetworktwitch.core.utils

class DefaultPaginator<T>(
    private val onLoad: (isLoading: Boolean, forceRefresh: Boolean) -> Unit,
    private val onRequest: suspend (nextPage: Int) -> ApiResult<List<T>>,
    private val onSuccess: (items: List<T>) -> Unit,
    private val onError: suspend (message: UiText) -> Unit,
) : Paginator<T> {
    private var page = 0
    override suspend fun loadNextItems(refreshing: Boolean) {
        onLoad(!refreshing, refreshing)
        when (val result = onRequest(page)) {
            is ApiResult.Success -> {
                val newItems = result.data.orEmpty()
                page++
                onSuccess(newItems)
                onLoad(false, false)
            }

            is ApiResult.Error -> {
                onError(result.uiText.orUnknownError())
                onLoad(false, false)
            }
        }

    }
}
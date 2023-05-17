package com.dracula.socialnetworktwitch.core.utils

class DefaultPaginator<T>(
    private val onLoad: (Boolean) -> Unit,
    private val onRequest: suspend (nextPage: Int) -> ApiResult<List<T>>,
    private val onSuccess: (items: List<T>) -> Unit,
    private val onError: (message: UiText) -> Unit,
) : Paginator<T> {
    private var page = 0
    override suspend fun loadNextItems() {
        onLoad(true)
        when (val result = onRequest(page)) {
            is ApiResult.Success -> {
                val newItems = result.data.orEmpty()
                page++
                onSuccess(newItems)
                onLoad(false)
            }

            is ApiResult.Error -> onError(result.uiText.orUnknownError())
        }

    }
}
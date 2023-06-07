package com.dracula.socialnetworktwitch.core.utils

data class PagingState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val refreshing: Boolean = false,
) {
    fun addNewItems(newItems: List<T>): PagingState<T> {
        return this.copy(
            isLoading = false,
            items = items + newItems,
            endReached = newItems.isEmpty(),
            refreshing = false
        )
    }
}

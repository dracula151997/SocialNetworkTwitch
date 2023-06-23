package com.dracula.socialnetworktwitch.core.utils

import com.dracula.socialnetworktwitch.core.presentation.utils.UiState

data class PagingState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val refreshing: Boolean = false,
) : UiState() {
    fun addNewItems(newItems: List<T>): PagingState<T> {
        return this.copy(
            isLoading = false,
            items = items + newItems,
            endReached = newItems.isEmpty(),
            refreshing = false
        )
    }

    fun addItem(newItem: T): PagingState<T> {
        if (items.contains(newItem)) return this
        return this.copy(
            isLoading = false,
            items = items + newItem,
            endReached = false,
            refreshing = false
        )
    }

    fun isNotEmpty() = items.isNotEmpty()

    val lastIndex get() = items.size - 1

    val moreThanOneItem get() = items.size > 1
}

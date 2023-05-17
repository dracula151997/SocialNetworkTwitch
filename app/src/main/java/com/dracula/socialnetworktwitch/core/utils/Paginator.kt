package com.dracula.socialnetworktwitch.core.utils

interface Paginator<T> {
    suspend fun loadNextItems()
}
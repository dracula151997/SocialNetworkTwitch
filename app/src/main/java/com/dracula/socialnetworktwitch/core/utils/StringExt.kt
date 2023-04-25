package com.dracula.socialnetworktwitch.core.utils

fun String?.orDefault(value: String): String {
    if (this == null) return value
    return this
}
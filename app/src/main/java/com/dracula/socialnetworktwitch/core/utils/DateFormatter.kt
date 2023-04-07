package com.dracula.socialnetworktwitch.core.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedString(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault())
        .run {
            format(this@toFormattedString)
        }
}
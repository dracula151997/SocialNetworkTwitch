package com.dracula.socialnetworktwitch.core.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toFormattedDate(pattern: String, locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat(pattern, locale)
        .run {
            format(this@toFormattedDate)
        }
}
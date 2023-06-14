package com.dracula.socialnetworktwitch.core.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dracula.socialnetworktwitch.R

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(@StringRes val id: Int) : UiText()
    class StringResourceWithArgs(@StringRes val id: Int, vararg val formatArgs: Any) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id)
            is StringResourceWithArgs -> context.getString(id, *formatArgs)
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id = id)
            is StringResourceWithArgs -> stringResource(id = id, *formatArgs)
        }
    }

    companion object {
        fun unknownError(): UiText = StringResource(R.string.error_unknown)
    }
}

fun UiText?.orUnknownError(): UiText {
    if (this == null) return UiText.unknownError()
    return this
}

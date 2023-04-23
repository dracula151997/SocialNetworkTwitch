package com.dracula.socialnetworktwitch.core.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp

/*
fun Dp.toPx(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this.value,
        Resources.getSystem().displayMetrics
    )
}
*/

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun Int.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
fun Dp.roundToPx() = with(LocalDensity.current) { this@roundToPx.roundToPx() }
@Composable
fun getStringResource(@StringRes id: Int?, vararg formatArgs: Any? = emptyArray()): String {
    return id?.let {
        if (formatArgs.isNotEmpty())
            stringResource(id = it, formatArgs)
        else stringResource(id = it)
    }.orEmpty()
}
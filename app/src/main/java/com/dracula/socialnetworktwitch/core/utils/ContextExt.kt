package com.dracula.socialnetworktwitch.core.utils

import android.content.Context
import android.content.Intent
import com.dracula.socialnetworktwitch.R

fun Context.sendSharePostIntent(postId: String) {
    val intent = Intent()
        .apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getString(
                    R.string.share_intent_text,
                    "social://dracula.com/post/details/$postId"
                )
            )
            type = "text/plain"
        }
    startActivity(Intent.createChooser(intent, "Share via"))

}
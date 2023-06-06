package com.dracula.socialnetworktwitch.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
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

fun Context.openUrlInBrowser(url: String) {
    var urlData = url
    if (!url.startsWith("http") || !url.startsWith("https"))
        urlData = "https://$url"
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(urlData)
    }
    startActivity(Intent.createChooser(intent, "Select an app"))
}
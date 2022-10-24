package dev.ushiekane.xmanager.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.install(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        type = "application/vnd.android.package-archive"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
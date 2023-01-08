package dev.ushiekane.xmanager.installer.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.os.Build
import dev.ushiekane.xmanager.installer.service.AppInstallService
import java.io.File

val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    PendingIntent.FLAG_MUTABLE
} else 0
val session = PackageInstaller.SessionParams(
    PackageInstaller.SessionParams.MODE_FULL_INSTALL
).apply { setInstallReason(PackageManager.INSTALL_REASON_USER) }

fun PackageInstaller.Session.writeApk(apk: File) {
    apk.inputStream().use { inputStream ->
        openWrite(apk.name, 0, apk.length()).use { outputStream ->
            inputStream.copyTo(outputStream, 1024 * 1024)
            fsync(outputStream)
        }
    }
}

fun Context.install(apk: File) {
    val session = packageManager.packageInstaller.openSession(
        packageManager.packageInstaller.createSession(session)
    )
    session.writeApk(apk)
    session.commit(
        PendingIntent.getService(
            this, 0, Intent(this, AppInstallService::class.java), intent
        ).intentSender
    )
    session.close()
}
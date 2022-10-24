package dev.ushiekane.xmanager.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import dev.ushiekane.xmanager.installer.AppInstallService
import dev.ushiekane.xmanager.installer.AppUninstallService
import java.io.File

fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
fun Context.openAppInfo(pkg: String) {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:" + pkg)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
fun Context.openApp(pkg: String) = startActivity(packageManager.getLaunchIntentForPackage(pkg))

fun Context.deleteCache() {
    cacheDir.deleteRecursively()
    Toast.makeText(this, "Successfully deleted!", Toast.LENGTH_SHORT).show()
}

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
fun Context.uninstall(name: String) = packageManager.packageInstaller.uninstall(
    name, PendingIntent.getService(
        this, 0, Intent(this, AppUninstallService::class.java), intent
    ).intentSender
)
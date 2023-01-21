package dev.ushiekane.xmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import dev.ushiekane.xmanager.R
import dev.ushiekane.xmanager.preferences.PreferencesManager
import dev.ushiekane.xmanager.ui.theme.Theme
import dev.ushiekane.xmanager.utils.showToast
import java.io.File

class SettingsViewModel(
    val app: Application,
    val prefs: PreferencesManager
): ViewModel() {
    fun checkLocation() {
        try {
            val file = File(prefs.downloadLocation!!)
            if (!file.canWrite() && !file.isDirectory && !file.exists()) {
                throw SecurityException()
            }
        } catch (e: Exception) {
            prefs.downloadLocation = "/storage/emulated/0/Download"
            app.showToast(R.string.invalid_location)
        }
    }

    fun setLocation(location: String) {
        prefs.downloadLocation = location
        checkLocation()
    }

    fun setTheme(theme: Theme) {
        prefs.theme = theme
    }

    fun cleanCacheDir() {
        app.cacheDir.deleteRecursively()
        app.showToast(R.string.cache_clean_successful)
    }
    init {
        checkLocation()
    }
    override fun onCleared() {
        checkLocation()
    }
}
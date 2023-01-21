package dev.ushiekane.xmanager.preferences

import android.content.SharedPreferences
import dev.ushiekane.xmanager.preferences.base.BasePreferenceManager
import dev.ushiekane.xmanager.ui.theme.Theme

class PreferencesManager(preferences: SharedPreferences): BasePreferenceManager(preferences) {
    var theme by enumPreference("theme", Theme.SYSTEM)
    var dynamicColor by booleanPreference("dynamic_color", true)
    var cloned by booleanPreference("cloned", false)
    var disableAds by booleanPreference("disable_ads", false)
    var autoInstall by booleanPreference("auto_install", false)
    var downloadLocation by stringPreference("download_location", "/storage/emulated/0/Download")
}
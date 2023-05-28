package dev.ushiekane.xmanager.preferences

import android.content.SharedPreferences
import dev.ushiekane.xmanager.preferences.base.BasePreferenceManager

class PreferencesManager(
    prefs: SharedPreferences
) : BasePreferenceManager(prefs) {
    var cloned by booleanPreference("cloned", false)
    var experimental by booleanPreference("experimental", false)
}
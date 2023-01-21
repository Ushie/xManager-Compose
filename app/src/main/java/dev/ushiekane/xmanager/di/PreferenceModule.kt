package dev.ushiekane.xmanager.di

import android.content.Context
import dev.ushiekane.xmanager.preferences.PreferencesManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val preferenceModule = module {
    fun providePreferences(context: Context): PreferencesManager {
        return PreferencesManager(context.getSharedPreferences("xmanager_preferences", Context.MODE_PRIVATE))
    }

    singleOf(::providePreferences)
}
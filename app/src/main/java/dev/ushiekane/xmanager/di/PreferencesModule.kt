package dev.ushiekane.xmanager.di

import android.content.Context
import dev.ushiekane.xmanager.preferences.PreferencesManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val preferencesModule = module {
    fun providePreferences(
        context: Context
    ) = PreferencesManager(context.getSharedPreferences("preferences", Context.MODE_PRIVATE))

    singleOf(::providePreferences)
}
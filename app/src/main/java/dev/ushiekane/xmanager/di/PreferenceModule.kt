package dev.ushiekane.xmanager.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val preferenceModule = module {
    fun providePreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("xmanager", Context.MODE_PRIVATE)

    singleOf(::providePreferences)
}
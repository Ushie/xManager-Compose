package dev.ushiekane.xmanager.di

import dev.ushiekane.xmanager.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SettingsViewModel)
}
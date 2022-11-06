package dev.ushiekane.xmanager.di

import com.download.library.DownloadImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val downloaderModule = module {
    single { DownloadImpl.getInstance(androidContext()) }
}
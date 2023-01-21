package dev.ushiekane.xmanager

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class XManagerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@XManagerApplication)
            modules(emptyList()) // TODO: Initialize app and add required modules here.
        }
    }
}
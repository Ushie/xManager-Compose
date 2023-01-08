package dev.ushiekane.xmanager

import android.app.Application
import dev.ushiekane.xmanager.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(httpModule, viewModelModule, repositoryModule)
        }
    }
}
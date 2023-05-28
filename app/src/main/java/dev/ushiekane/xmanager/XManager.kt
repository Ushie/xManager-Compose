package dev.ushiekane.xmanager

import android.app.Application
import dev.ushiekane.xmanager.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class XManager : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@XManager)
            modules(httpModule, viewModelModule, repositoryModule, preferenceModule)
        }
    }
}
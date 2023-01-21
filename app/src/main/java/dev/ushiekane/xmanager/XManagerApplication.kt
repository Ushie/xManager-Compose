package dev.ushiekane.xmanager

import android.app.Application
import dev.ushiekane.xmanager.di.preferenceModule
import dev.ushiekane.xmanager.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class XManagerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@XManagerApplication)
            modules(
                preferenceModule,
                viewModelModule
             )
        }
    }
}
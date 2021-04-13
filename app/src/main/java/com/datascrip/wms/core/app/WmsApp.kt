package com.datascrip.wms.core.app

import android.app.Application
import android.content.Context
import com.datascrip.wms.core.di.NetworkApp
import com.datascrip.wms.core.di.appModule
import com.datascrip.wms.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WmsApp : Application() {

    companion object{
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this@WmsApp
        startKoin {
            androidContext(appContext)
            modules(NetworkApp().networkModule
                    + viewModelModule
                    + appModule
            )
        }
    }

}
package com.datascrip.wms.core.app

import android.app.Application
import android.content.Context

class WmsApp : Application() {

    companion object{
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this@WmsApp
    }

}
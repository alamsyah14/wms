package com.datascrip.wms.core.di

import com.datascrip.wms.BuildConfig
import com.datascrip.wms.core.base.createOkHttpClient
import com.datascrip.wms.core.base.createRetrofit
import org.koin.dsl.module


class NetworkApp() {

    val networkModule = module(override = true) {
        single {
            createOkHttpClient(
                BuildConfig.DEBUG
            )
        }
        single {
            createRetrofit(
                get(),
                BuildConfig.BASE_URL
            )
        }
    }

}
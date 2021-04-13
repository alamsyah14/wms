package com.datascrip.wms.core.di

import com.datascrip.wms.core.util.DeviceIdHelper
import com.datascrip.wms.core.util.ImageStoreUtil
import com.datascrip.wms.core.util.LocalFileHelper
import com.datascrip.wms.core.util.PreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { LocalFileHelper(androidContext()) }
    single { DeviceIdHelper(localFileHelper = get()) }
    single { PreferenceUtils(androidContext()) }
    single { ImageStoreUtil(androidContext()) }
}
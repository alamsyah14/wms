package com.datascrip.wms.core.di

import com.datascrip.wms.feature.entrance.EntranceVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module(override = true) {
    viewModel { EntranceVM(get()) }
}
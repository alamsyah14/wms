package com.datascrip.wms.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OptionSelected(
    val order: Int,
    val key: String,
    val name: String
) :Parcelable
package com.datascrip.wms.core.extention

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat
import com.datascrip.wms.core.util.LANG_PREF
import com.datascrip.wms.core.util.LocalizationUtil
import com.datascrip.wms.core.util.PreferenceUtils

fun View.getDimensionPx(@DimenRes dimen : Int) : Int {
    return resources.getDimensionPixelSize(dimen)
}

fun TextView.setTextStyle(@StyleRes styleRes : Int) {
    TextViewCompat.setTextAppearance(this,styleRes)

}

fun ViewGroup.inflate(@LayoutRes layoutRes : Int) : View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Context.getLang(): String {
    return PreferenceUtils(this).loadFromPrefs(
        LANG_PREF,
        defaultValue = LocalizationUtil.DEFAULT_LOCALE_LANGUAGE
    ) ?: LocalizationUtil.DEFAULT_LOCALE_LANGUAGE
}
package com.datascrip.wms.core.util

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*

object LocalizationUtil {

    const val DEFAULT_LOCALE_LANGUAGE = "en"

    @Suppress("DEPRECATION")
    fun applyLanguageContext(context: Context, language: String): Context {
        val resource = context.resources
        val locale = Locale(language.toLowerCase(Locale.ROOT))
        Locale.setDefault(locale)

        val config = resource.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }

        return if (Build.VERSION_CODES.JELLY_BEAN_MR1.isAtLeastSdkVersion()) {
            context.createConfigurationContext(config)
        } else {
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
            ContextWrapper(context)
        }
    }

    private fun Int.isAtLeastSdkVersion(): Boolean {
        return Build.VERSION.SDK_INT >= this
    }

}
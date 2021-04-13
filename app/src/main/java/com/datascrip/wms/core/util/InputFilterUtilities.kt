package com.datascrip.wms.core.util

import android.text.InputFilter

class InputFilterUtilities {
    companion object {
        /**
         * Filter OneTextFieldChar
         */
        var filterOneTextField = InputFilter { source, _, _, _, _, _ ->
            val stringTemp = source.toString()
            stringTemp.forEach { char ->
                val charTemp = char.toString()
                if (!charTemp.validOneTextFieldChar())
                    return@InputFilter stringTemp.replace(charTemp, "")
            }
            null
        }
    }
}

fun CharSequence.validOneTextFieldChar() =
    this.isNotEmpty() && RegexPatterns.OneTextFieldChar.matches(this)
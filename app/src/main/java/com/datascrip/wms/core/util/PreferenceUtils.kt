package com.datascrip.wms.core.util

import android.content.Context
import android.content.SharedPreferences

const val PREF_NAME = ".core-data"
const val LANG_PREF = ".lang-pref"

class PreferenceUtils(val context: Context) {

    fun saveToPrefs(key: String, content: Any) {
        val editor= context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        when(content){
            is String -> editor.putString(key, content)
            is Boolean -> editor.putBoolean(key, content)
            is Int -> editor.putInt(key, content)
            else -> editor.putString(key, content.toString())
        }
        editor.apply()
    }

    fun loadFromPrefs(key: String, defaultValue: String? = ""): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(key, defaultValue)
    }

    fun loadIntFromPrefs(key: String): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(key, 0)
    }

    fun loadBoolFromPref(key: String): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(key, false)
    }

    fun removeFromPrefs(key: String) {
        val editor: SharedPreferences.Editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.remove(key)
        editor.apply()
    }

    fun removeAllPrefs(){
        val editor= context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }

}
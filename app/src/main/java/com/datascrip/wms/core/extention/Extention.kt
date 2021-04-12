package com.datascrip.wms.core.extention

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.datascrip.wms.core.util.StateWrapper
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat

inline fun <T> LifecycleOwner.subscribeSingleState(
    liveData: LiveData<StateWrapper<T>>,
    crossinline onEventUnhandled: (T) -> Unit
) {
    liveData.observe(this, Observer { it?.getEventIfNotHandled()?.let(onEventUnhandled) })
}

fun <T> T?.isNUll(): Boolean { return this == null }

fun <T> T?.isNotNull() : Boolean = this != null

@SuppressLint("SimpleDateFormat")
fun convertTimeToDate(isoTime: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    lateinit var formattedDate: String
    try {
        val convertedDate = sdf.parse(isoTime)
        formattedDate = SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(convertedDate!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return formattedDate
}

fun validateURLString(urlString: String): Boolean {
    return try {
        val url = URL(urlString)
        url.toURI()
        true
    } catch (exception: Exception) {
        false
    }
}


fun isOnline(context: Context): Boolean {
    val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
    }
    return false
}
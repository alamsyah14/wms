package com.datascrip.wms.core.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject

class ResponseInterceptor(private val isDebug: Boolean): Interceptor {

    private val TAG = "wms-datascrip-api"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(chain.request())
        if(isDebug) {
            addResponseLog(request, response)
        }

        return response
    }

    private fun addResponseLog(request: Request, response: Response) {
        Log.d(TAG, "-----RESPONSE----")
        Log.d(TAG, "Url: ${request.url}")
        Log.d(TAG, "Http Code: ${response.code}")
        if (response.body != null) {
            Log.d(TAG, "Body: ${response.peekBody(Long.MAX_VALUE).string()}")
        }
        Log.d(TAG, "----------------")
    }

}

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}
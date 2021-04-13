package com.datascrip.wms.core.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class RequestInterceptor (private val isDebug: Boolean ): Interceptor {

    private val JSON = "application/json"
    private val TAG = "wms-datascrip-api"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val listHeader = createHeader()
        val newRequestBuilder = request.newBuilder()

        listHeader.forEach { newRequestBuilder.header(it.first, it.second) }
        if(isDebug) {
            addLogRequest(request)
        }
        return chain.proceed(newRequestBuilder.build())
    }

    private fun createHeader(): List<Pair<String, String>> {
        val listHeader = mutableListOf(
            "Content-Type" to JSON,
            "Accept" to JSON
        )

        return listHeader
    }

    private fun addLogRequest(request: Request) {
        Log.d(TAG, "-----REQUEST----")
        Log.d(TAG, "Url: ${request.url}")
        request.headers.forEach {
            Log.d(TAG, "Headers: ${it.first} : ${it.second}")
        }
        Log.d(TAG, "Body: ${request.body.bodyToString()}")
        Log.d(TAG, "----------------")
    }

}

// provide escaped json body string
fun RequestBody?.bodyToString(): String {
    if (this == null) return ""
    val buffer = okio.Buffer()
    writeTo(buffer)
    return buffer.readUtf8()
}

fun Request.Builder.addSecureHeader(methodName: String, requestBody: String, relativeUrl: String) {

}
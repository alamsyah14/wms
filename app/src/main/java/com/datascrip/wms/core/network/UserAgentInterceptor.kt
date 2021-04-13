package com.datascrip.wms.core.network

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.userAgent

class UserAgentInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestWithUserAgent = originRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }

}
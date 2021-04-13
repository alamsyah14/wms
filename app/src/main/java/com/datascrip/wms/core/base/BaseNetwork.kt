package com.datascrip.wms.core.base

import com.datascrip.wms.core.model.BaseResponse
import com.datascrip.wms.core.network.*
import com.datascrip.wms.core.util.ResponseCode
import com.google.gson.JsonParseException
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


sealed class ResultCall<out T> {
    data class Success<T>(val statusCode: String = ResponseCode.SUCCESS, val data: T) : ResultCall<T>()
    data class Failed(val responseCode: String) : ResultCall<Nothing>()
    data class Error(val exc: Exception, val errorMessage: String) : ResultCall<Nothing>()
}

fun createOkHttpClient(isDebug: Boolean): OkHttpClient {
    val httpLogInterceptor = HttpLoggingInterceptor()
    httpLogInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

    return OkHttpClient.Builder().apply {
        addInterceptor(UserAgentInterceptor())
        addInterceptor(RequestInterceptor(isDebug))
        if(isDebug) {
            addInterceptor(httpLogInterceptor)
        }
        addInterceptor(
            ResponseInterceptor(isDebug)
        )
    }.connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

inline fun <reified T> createApi(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

suspend fun <T> callAwait(
    apiCall: suspend () -> BaseResponse<T>
): ResultCall<T?> {
    return try {
        val callResult = apiCall()
        if (callResult.response_code == ResponseCode.SUCCESS) {
            ResultCall.Success(
                callResult.response_code,
                callResult.data
            )
        } else {
            ResultCall.Failed(callResult.response_code)
        }
    } catch (exc: HttpException) {
        val error = exc.response()

        ResultCall.Failed((error?.code() ?: 0).toString())
    } catch (exc: SocketTimeoutException) {
        ResultCall.Error(
            exc,
            "Connection Timeout"
        )
    } catch (exc: IOException) {
        ResultCall.Error(
            exc,
            "No Internet Connection"
        )
    } catch (exc: Exception) {
        ResultCall.Error(
            exc,
            "Internal Server Error"
        )
    } catch (exc: JsonParseException) {
        ResultCall.Error(
            exc,
            "Internal Server Error"
        )
    }
}

fun <A, B> ResultCall<A?>.mapTo(block: (A) -> B): ResultCall<B> {
    return when (this) {
        is ResultCall.Success -> {
            val newData = this.data ?: Unit as A
            ResultCall.Success(
                this.statusCode,
                block(newData)
            )
        }
        is ResultCall.Failed -> ResultCall.Failed(
            this.responseCode
        )
        is ResultCall.Error -> ResultCall.Error(
            this.exc,
            this.errorMessage
        )
    }
}
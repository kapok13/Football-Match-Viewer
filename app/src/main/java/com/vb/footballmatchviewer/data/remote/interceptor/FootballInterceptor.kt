package com.vb.footballmatchviewer.data.remote.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import com.vb.footballmatchviewer.BuildConfig
import okhttp3.ResponseBody.Companion.toResponseBody

class FootballInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("x-apisports-key", BuildConfig.FOOTBALL_API_KEY)
            .build()

        val response = chain.proceed(newRequest)

        val responseBody = response.body
        val responseBodyString = responseBody?.string() ?: ""

        Log.d("HTTP_RESPONSE", """
            >>> URL: ${request.url}
            >>> Method: ${request.method}
            >>> Headers: ${request.headers}
            >>> Body:
            $responseBodyString
        """.trimIndent())

        return response.newBuilder()
            .body(responseBodyString.toResponseBody(responseBody?.contentType()))
            .build()
    }
}

package com.ling.kotlin.retroft

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class FilterInterceptor : Interceptor{
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpBuilder =originalRequest.url().newBuilder()
//        httpBuilder.addEncodedQueryParameter(
//            HttpConfig.KEY,
//            HttpConfig.KEY_MAP
//        )
        val requestBuilder = originalRequest.newBuilder().url(httpBuilder.build())
        //封装公用的静态header头
        requestBuilder.addHeader("Client-type", HttpConfig.CLIENT_TYPE)
        requestBuilder.addHeader("Content-Type", HttpConfig.CONTENT_TYPE)
        return chain.proceed(requestBuilder.build())
    }
}
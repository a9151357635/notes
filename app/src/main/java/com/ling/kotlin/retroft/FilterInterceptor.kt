package com.ling.kotlin.retroft

import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.utils.CacheUtils
import com.ling.kotlin.utils.NetUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class FilterInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpBuilder = originalRequest.url.newBuilder()
        val requestBuilder = originalRequest.newBuilder().url(httpBuilder.build())
        //封装公用的静态header头
        requestBuilder.addHeader("Client-type", HttpConfig.CLIENT_TYPE)
        requestBuilder.addHeader("Content-Type", HttpConfig.CONTENT_TYPE)
        requestBuilder.addHeader("os", "Android")
        requestBuilder.addHeader("osVersion",AppUtils.getSystemVersion())
        requestBuilder.addHeader("deviceId", AppUtils.getDeviceId())
        requestBuilder.addHeader("version", AppUtils.getVersionName())
        requestBuilder.addHeader("userAgent", AppUtils.getSystemModel())
        requestBuilder.addHeader("x-platform-id","185473384870600704")
        requestBuilder.addHeader("apn", NetUtils.getNetworkState())//网络类型
        requestBuilder.addHeader("ts", (System.currentTimeMillis()/1000).toString())//时间戳
        CacheUtils.getToken()?.let {
            requestBuilder.addHeader("token", it)//token 用户登陆令牌
        }
        return chain.proceed(requestBuilder.build())
    }
}
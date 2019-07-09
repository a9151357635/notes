package com.ling.kotlin.retroft

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.ling.kotlin.retroft.converter.GsonConvertFactory
import com.ling.kotlin.retroft.converter.IntegerDefaultAdapter


class RetrofitManagement private constructor(){
    companion object{
        private const val READ_TIMEOUT:Long =60000
        private const val WRING_TIMEOUT:Long = 60000
        private const val CONNECT_TIMEOUT:Long = 60000
        val instance: RetrofitManagement by lazy(LazyThreadSafetyMode.SYNCHRONIZED){ RetrofitManagement() }
    }
    private val serverMap = ConcurrentHashMap<String,Any>()

    /**
     * Retrofit和RX结合
     */
    private fun createRetrofit(url:String):Retrofit{
        //设置自定义的okhttpclient
        val builder = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT,TimeUnit.MILLISECONDS)
            .writeTimeout(WRING_TIMEOUT,TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT,TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(FilterInterceptor())
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(httpLoggingInterceptor)
        //可添加抓包
//        builder.addInterceptor(M)
        val client = builder.build()
        //Retrofit和RX结合
        return Retrofit.Builder().client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConvertFactory.create()) //自定义Converter
//            .addConverterFactory(GsonConverterFactory.create()) //自带converter解析
//            .addConverterFactory(GsonConverterFactory.create(buildGson())) //自定义解析gson
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    /**
     * 从map集合中获取代理对象并通过retrofit创建
     */
    fun <T :Any> getService(clz:Class<T>,host:String):T{
        if(serverMap.containsKey(host)){
            val obj = serverMap[host] as? T
            obj?.let { return it }
        }
        val value = createRetrofit(host).create(clz)
        serverMap[host] = value
        return  value
    }

    fun buildGson(): Gson {
        return  GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Int::class.java, IntegerDefaultAdapter())
            .registerTypeAdapter(Int::class.javaPrimitiveType, IntegerDefaultAdapter())
            .create()
    }
}

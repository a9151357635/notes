//package com.ling.kotlin.retroft.converter
//
//import com.google.gson.*
//import com.google.gson.reflect.TypeToken
//import com.ling.kotlin.retroft.*
//import okhttp3.MediaType
//import okhttp3.RequestBody
//import okhttp3.ResponseBody
//import okhttp3.internal.Util.UTF_8
//import okio.Buffer
//import retrofit2.Converter
//import retrofit2.Retrofit
//import java.io.ByteArrayInputStream
//import java.io.IOException
//import java.io.InputStreamReader
//import java.io.OutputStreamWriter
//import java.lang.reflect.Type
//import java.nio.charset.Charset
//
//
///**
// * 自定义gson解析，主要是解析服务器返回失败的json数据
// * 如下：
// * 服务器数据返回成功如下：{"status":1,"msg":"请求成功","data":{……}}
// * 失败如下：{"code":403,"msg":"请求TOKEN失效","data":{},"status":"error","error":false}
// */
//class GsonConvertFactory private constructor(private val gson: Gson) : Converter.Factory() {
//
//    init {
//        if (gson == null) throw NullPointerException("gson == null")
//    }
//
//    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
//        val adapter = gson.getAdapter(TypeToken.get(type))
//        return GsonRequestBodyConverter(gson, adapter)
//    }
//
//    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): InterceptGsonResponseBodyConverter<out Any> {
//        val adapter = gson.getAdapter(TypeToken.get(type))
//        return InterceptGsonResponseBodyConverter(gson, adapter)
//    }
//
//    companion object {
//        fun create(gson: Gson = Gson()): GsonConvertFactory {
//            return GsonConvertFactory(gson)
//        }
//    }
//}
//
///**
// * 未修改跟原来的
// * GsonConverterFactory #GsonResponseBodyConverter一样的
// */
// class GsonRequestBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {
//
//    @Throws(IOException::class)
//    override fun convert(value: T): RequestBody {
//        val buffer = Buffer()
//        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
//        val jsonWriter = gson.newJsonWriter(writer)
//        adapter.write(jsonWriter, value)
//        jsonWriter.close()
//        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
//    }
//
//    companion object {
//        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
//        private val UTF_8 = Charset.forName("UTF-8")
//    }
//}
//
///**
// *
// * 自定义响应类  修改做拦截抛出操作 应用到Gson反序列和序列化部分的知识
// * 跟比原来的了一个拦截器，方便实现自定义业务的要求
// */
//class InterceptGsonResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {
//
//    @Throws(IOException::class)
//    override fun convert(value: ResponseBody): T {
//        val response = value.string()
//        val httpStatus = gson.fromJson(response, BaseResponse::class.java)
//        //token 失效，报错token失效异常
//        if(httpStatus.code == HttpConfig.CODE_TOKEN_INVALID){
//            value.close()
//            throw TokenInvalidResultException(
//                httpStatus.msg ?: "token失效，请重新登陆",
//                httpStatus.code
//            )
//        }
//        //其他错误信息
//        if(httpStatus.code != HttpConfig.CODE_SUCCESS){
//            value.close()
//            throw ServerResultException(httpStatus.msg ?: "未知错误", httpStatus.code)
//        }
////        //返回{}抛出异常，因为解析不到，暂定这样吧
////        if(httpStatus.code ==  HttpConfig.CODE_SUCCESS && httpStatus.data.toString() =="{}"){
////            value.close()
////            throw ServerResultException(httpStatus.msg ?: "成功", httpStatus.code)
////        }
//        //继续处理body数据反序列化，注意value.string() 不可重复使用
//        val contentType = value.contentType()
//        val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
//        val inputStream = ByteArrayInputStream(response.toByteArray())
//        val reader = InputStreamReader(inputStream, charset)
//        val jsonReader = gson.newJsonReader(reader)
//        value.use {
//            return adapter.read(jsonReader)
//        }
////        value.use { value ->
////            return adapter.read(jsonReader)
////        }
//    }
//}
//
//

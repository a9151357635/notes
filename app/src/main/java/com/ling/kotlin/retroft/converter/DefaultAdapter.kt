package com.ling.kotlin.retroft.converter

import com.google.gson.*
import com.ling.kotlin.retroft.BaseResponse
import java.lang.NumberFormatException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 针对Long的解析，先写一个解析适配器，实现JsonSerializer, JsonDeserializer
 * 重写解析方法，先尝试用String类型解析，如果等于空字符串""，则返回0值
 * 否则再尝试用整型解析，并且catch数字格式异常转成Json解析异常抛出
 */
class LongDefaultAdapter : JsonSerializer<Long>,JsonDeserializer<Long>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Long? {
        try {
            return  if(json?.toString().equals("")) 0 else json?.asLong
        }catch (e:NumberFormatException){
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(src: Long?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }
}


/**
 * 针对Int的解析，先写一个解析适配器，实现JsonSerializer, JsonDeserializer
 * 重写解析方法，先尝试用String类型解析，如果等于空字符串""，则返回0值
 * 否则再尝试用整型解析，并且catch数字格式异常转成Json解析异常抛出
 */
class IntegerDefaultAdapter : JsonSerializer<Int>,JsonDeserializer<Int>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Int? {
        try {
            return  if(json?.toString().equals("")) 0 else json?.asInt
        }catch (e:NumberFormatException){
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(src: Int?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }
}


/**
 * 添加反序列化
 *  服务器数据返回成功如下：{"status":1,"msg":"请求成功","data":{……}}
 * 失败如下：{"code":403,"msg":"请求TOKEN失效","data":{},"status":"error","error":false}
 */
class ResultJsonDeserializer : JsonDeserializer<BaseResponse<*>> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type, context: JsonDeserializationContext): BaseResponse<*> {
        val response: BaseResponse<*> = BaseResponse(0, "", null)
        json?.let {
            if (json.isJsonObject) {
                val jsonObject = json.asJsonObject
                val code = jsonObject.get("code").asInt
                response.code = code
                response.msg = jsonObject.get("msg").toString()
                if (code != 200) {
                    return response
                }
                val itemType = (typeOfT as ParameterizedType).actualTypeArguments[0]
                val dataStr = jsonObject.get("data").toString()
                response.data = context.deserialize(if(dataStr.isNull())jsonObject.get("msg") else jsonObject.get("data"), itemType)
            }
        }
        return response
    }

    /**
     * 扩展函数
     */
    private fun String.isNull():Boolean{
        return this =="{}" ||  this == "[]" ||  this ==""
    }
}


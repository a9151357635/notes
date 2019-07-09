package com.ling.kotlin.retroft.converter

import com.google.gson.*
import java.lang.NumberFormatException
import java.lang.reflect.Type

/**
 * 针对整型的解析，先写一个解析适配器，实现JsonSerializer, JsonDeserializer
重写解析方法，先尝试用String类型解析，如果等于空字符串""，则返回0值
否则再尝试用整型解析，并且catch数字格式异常转成Json解析异常抛出
 */
class IntegerDefaultAdapter : JsonSerializer<Int>,JsonDeserializer<Int>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Int? {
        try {
            return  if(json?.asString.equals("")) 0 else json?.asInt
        }catch (e:NumberFormatException){
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(src: Int?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }

}
package com.ling.kotlin.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import java.util.ArrayList

/**
 * Created by Owner on 2018/3/29.
 */

object JSonUtils {

    /**
     * 解析Object
     * @param text
     * @param classz
     * @param <T>
     * @return
    </T> */
    fun <T> parseObject(text: String, classz: Class<T>): T? {
        var result: T? = null

        try {
            result = Gson().fromJson(text, classz)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    /***
     * 解析集合
     * @param text
     * @param classz
     * @param <T>
     * @return
    </T> */
    fun <T> parseArray(text: String, classz: Class<T>): List<T> {
        val list = ArrayList<T>()
        try {
            val parser = JsonParser()
            val jsonArray = parser.parse(text).asJsonArray
            val gson = Gson()
            for (element in jsonArray) {
                val result = gson.fromJson(element, classz)
                list.add(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }


    /***
     * 解析集合（字符串带key）
     * @param text
     * @param classz
     * @param <T>
     * @return
    </T> */
    fun <T> parseArrayStartWithKey(text: String, key: String, classz: Class<T>): List<T> {
        val list = ArrayList<T>()
        try {
            //先转JsonObject
            val jsonObject = JsonParser().parse(text).asJsonObject
            val jsonArray = jsonObject.getAsJsonArray(key)
            val gson = Gson()
            for (element in jsonArray) {
                val result = gson.fromJson(element, classz)
                list.add(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }
}

package com.ling.kotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import com.ling.kotlin.LotteryApp
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigDecimal
import java.util.regex.Pattern


object AppUtils{


    fun isNetworkAvailable(context: Context): Boolean {
        val  cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return  networkInfo.isConnected
    }

    fun matcherIphoneNumber(iphone: String): Boolean {
        //电话正则表达式
        val p = Pattern.compile("^((10[0-9])|(11[0-9])|(12[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$")
        return p.matcher(iphone).matches()
    }

    /**
     * 截取俩位小说点
     * @param text
     * @return
     */
    fun decimalFormat(text: String): String {
        var textNew = text
        if (TextUtils.isEmpty(textNew) || "0.000000" == textNew || "0.0000" == textNew) {
            return "0.00"
        }
        if (textNew.contains(",")) {
            textNew = textNew.replace(",", "")
        }
        val bd = BigDecimal(textNew)
        val setScale = bd.setScale(2, BigDecimal.ROUND_DOWN)
        return setScale.toString()
    }

    fun getAssetStr(file_name: String): String {
        if (TextUtils.isEmpty(file_name)) {
            return ""
        }
        return LotteryApp.instance!!.assets.open(file_name).bufferedReader().use {
            it.readText()
        }
    }

}

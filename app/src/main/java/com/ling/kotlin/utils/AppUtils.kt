package com.ling.kotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.widget.TextView
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
        return ContextUtils.context.assets.open(file_name).bufferedReader().use {
            it.readText()
        }
    }
    /**
     * 改变TextView某个字体的颜色值 带有string文件占位符
     *
     * @param colorStr
     * @param resStringId   string字符串Id
     * @param changeContent 需要改变的内容
     * @param mTextView     展示控件
     */
    fun changeTextColor( context: Context,colorStr: String,resStringId: Int,changeContent: String, mTextView: TextView?) {
        mTextView?.let {
            val str = "<font color='$colorStr'>$changeContent</font>"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) it.text = Html.fromHtml(context.getString(resStringId, str), Html.FROM_HTML_MODE_COMPACT) else it.text = Html.fromHtml(context.getString(resStringId, str))
        }
    }

}

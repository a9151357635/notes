package com.ling.kotlin.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.Html
import android.text.TextUtils
import android.widget.TextView
import com.ling.kotlin.BuildConfig
import java.math.BigDecimal
import java.util.regex.Pattern


object AppUtils{


    fun isNetworkAvailable(context: Context): Boolean {
        val  cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        networkInfo.subtype
        return  networkInfo.isConnected
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
     fun getSystemVersion():String{
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun  getSystemModel() :String{
        return Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    fun getDeviceBrand():String{
        return Build.BRAND
    }

    /**
     * 获取IMEI标识(手机唯一的标识)
     *
     * @param context
     * @return
     */
    fun getDeviceId(): String {
        val tm = ContextUtils.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var device_id = tm.deviceId
        //如果Android Pad没有IMEI,用此方法获取设备ANDROID_ID
        if (TextUtils.isEmpty(device_id)) {
            device_id = android.provider.Settings.Secure.getString( ContextUtils.context.contentResolver,android.provider.Settings.Secure.ANDROID_ID)
        }
        return device_id
    }

    /**
     * 获得版本号信息
     * @return
     */
    fun getVersionName(): String{
        val pm =  ContextUtils.context.packageManager
        try {
            val info = pm.getPackageInfo(ContextUtils.context.packageName, 0)
            return info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }


    fun matcherIphoneNumber(iphone: String): Boolean {
        //电话正则表达式
        val p = Pattern.compile("^((10[0-9])|(11[0-9])|(12[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$")
        return p.matcher(iphone).matches()
    }
    /**
     * 检测账号必须为6-15位数字与字母的组合
     * @param userAccount
     * @return
     */
    fun matcherAccount(userAccount: String): Boolean {
        val pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$")
        return pattern.matcher(userAccount).matches()
    }
    /**
     * 判断字符串是否含有中文
     * @param str
     * @return
     */
    fun isContainChinese(str: String): Boolean {
        return Pattern.compile("[\u4e00-\u9fa5]").matcher(str).find()
    }

    /**
     * 检测是否为中文
     * @param name
     * @return
     */
    fun matcherZh(name: String): Boolean {
        //中文正则表达式--少数民族名字
        return if (name.contains("·") || name.contains("•")) {
            name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$".toRegex())
        } else {
            name.matches("^[\\u4e00-\\u9fa5]+$".toRegex())
        }
    }

    fun getAppColor(): String {
        return when {
            "k8" == BuildConfig.APP_TYPE -> "#3094DF"
            "ylc" == BuildConfig.APP_TYPE -> "#F50B78"
            else -> "#f6673d"
        }
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

    fun isYLCOrCXC(): Boolean {
        return "ylc" == BuildConfig.APP_TYPE || "cxc" == BuildConfig.APP_TYPE
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

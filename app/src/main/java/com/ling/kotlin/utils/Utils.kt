package com.ling.kotlin.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import com.ling.kotlin.LotteryApp
import com.ling.kotlin.retroft.HttpConfig
import android.util.DisplayMetrics
import android.app.Activity
import io.paperdb.Paper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class ContextUtils{
    companion object{
        val context:Context by lazy { LotteryApp.instance }
    }
}

class ToastUtils{
    companion object{
        fun showToast(context: Context = ContextUtils.context,msg:String){
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
        }
    }
}

class HeaderMapUtils{
    companion object{
         fun commenHeader():Map<String,String>{
            val  headerMap = HashMap<String,String>()
            headerMap["Staffid"] = HttpConfig.KEY_MAP
            headerMap["Timestamp"] = (System.currentTimeMillis()/1000).toString()
            headerMap["username"] = "ak1234"
            headerMap["token"] = "3ae4a07d050f4358a3f6f3f84656381a"
            return headerMap
        }
    }
}

class TimerUtils(millisInFuture: Long, countDownInterval: Long, private var listener: TimerTaskListener?) :CountDownTimer(millisInFuture, countDownInterval) {
    override fun onTick(millisUntilFinished: Long) {
    }
    override fun onFinish() {
        listener?.run()
    }

    interface TimerTaskListener {
        fun run()
    }
}

object DisplayUtils {

    //将pixel转换成dip(dp)
    fun px2Dp(context: Context, px: Float): Int {
        val density = context.resources.displayMetrics.density
        return (px / density + 0.5f).toInt()
    }

    //将dip(dp)转换成pixel
    fun dp2Px(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    //将pixel转换成sp
    fun px2Sp(context: Context, px: Float): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (px / scaledDensity + 0.5f).toInt()
    }

    //将sp转换成pixel
    fun sp2Px(context: Context, sp: Float): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (sp * scaledDensity + 0.5f).toInt()
    }

    // 屏幕宽度（像素）
    fun getWindowWidth(context: Activity): Int {
        val metric = DisplayMetrics()
        context?.windowManager.defaultDisplay.getMetrics(metric)
        return metric.widthPixels
    }

    // 屏幕高度（像素）
    fun getWindowHeight(context: Activity): Int {
        val metric = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(metric)
        return metric.heightPixels
    }

}

object CacheUtils{

    fun saveFollowLottery(followList:MutableList<Int>){
        Paper.book().write("lotterys",followList)
    }

    fun getFollowLottery():MutableList<Int>{

        try {
            return Paper.book().read("lotterys")
        }catch (e:Exception){

        }
        return mutableListOf()
    }
}

object DateUtils{
    private const val YYYY_MM_DD = "yyyy-MM-dd"
    private const val YY_MM_DD_HH = "yy-MM-dd HH"
    private const val YYYY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss"
    private const val YYYY_MM_DD_Y_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss"
    fun getTimeYYYYMMDD(date: Date?): String {//可根据需要自行截取数据显示
        return if (date == null) {
            ""
        } else getSDF(YYYY_MM_DD).format(date)
    }
    private fun getSDF(pattern: String): SimpleDateFormat {
        return SimpleDateFormat(pattern)
    }

    fun getDate(time: String): Date? {
        try {
            return getSDF(YYYY_MM_DD_Y_HH_MM_SS).parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

}
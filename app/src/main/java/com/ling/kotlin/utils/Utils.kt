package com.ling.kotlin.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import com.ling.kotlin.LotteryApp
import com.ling.kotlin.retroft.HttpConfig
import android.util.DisplayMetrics
import android.app.Activity
import com.ling.kotlin.R
import io.paperdb.Paper
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
         fun commonHeader():Map<String,String> =  mapOf("Staffid" to HttpConfig.KEY_MAP,"Timestamp" to (System.currentTimeMillis()/1000).toString(),"username" to "ak1234","token" to "5401da9e67f0427c9212eb601a44be51")
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
        return try {Paper.book().read("lotterys")}catch (e:Exception){mutableListOf()}
    }
}

object DateUtils{
    private const val YYYY_MM_DD = "yyyy-MM-dd"
    private const val YY_MM_DD_HH = "yy-MM-dd HH"
    private const val YYYY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss"
    fun getTimeYYYYMMDD(date: Date?): String {//可根据需要自行截取数据显示
        return if (date == null) {
            ""
        } else getSDF(YYYY_MM_DD).format(date)
    }

    private fun getSDF(pattern: String): SimpleDateFormat {
        return SimpleDateFormat(pattern)
    }
}


object BankUtils {
    /**
     * 各个银行的图标
     */
    val bankMap = mapOf(
        "ICBC" to R.drawable.ic_bank_icbc,//工商银行
        "CMB" to R.drawable.ic_bank_cmb,//招商银行
        "CCB" to R.drawable.ic_bank_ccb,//建设银行
        "ABC" to R.drawable.ic_bank_abc,//农业银行
        "BOC" to R.drawable.ic_bank_boc,//中国银行
        "CMBC" to R.drawable.ic_bank_cmbc,//民生银行
        "CEB" to R.drawable.ic_bank_ceb,//光大银行
        "BCM" to R.drawable.ic_bank_bcm,//交通银行
        "SPDB" to R.drawable.ic_bank_spdb, //浦发银行
        "PAB" to R.drawable.ic_bank_pab, //平安银行
        "CIB" to R.drawable.ic_bank_cib,//兴业银行
        "CNCB" to R.drawable.ic_bank_zxb,//中信银行
        "ZXB" to R.drawable.ic_bank_zxb,//中信银行
        "GDB" to R.drawable.ic_bank_gdb,//广发银行
        "PSBC" to R.drawable.ic_bank_psbc,//邮政银行
        "HXB" to R.drawable.ic_bank_hxb, //华夏银行
        "RCB" to R.drawable.ic_bank_rcb,//浙江农商
        "CDYH" to R.drawable.ic_bank_cdyh,//成都银行
        "CZB" to R.drawable.ic_bank_czb,//浙商银行
        "ZJNCXYS" to R.drawable.ic_bank_czb,//浙商银行
        "BOJ" to R.drawable.ic_bank_boj,//锦州银行
        "SJB" to R.drawable.ic_bank_sjb,//盛京银行
        "LCC" to R.drawable.ic_bank_lcc,//辽宁农村信用社
        "LTD" to R.drawable.ic_bank_ltd,//吉林银行
        "BOD" to R.drawable.ic_bank_bod,//大连银行
        "DLB" to R.drawable.ic_bank_bod,//大连银行
        "BOH" to R.drawable.ic_bank_boh,//葫芦岛银行
        "HBB" to R.drawable.ic_bank_hbb,//哈尔滨银行
        "BRCB" to R.drawable.ic_bank_brcb,//北京农村商业银行
        "SRCB" to R.drawable.ic_bank_srcb,//上海农商银行
        "ZJKYH" to R.drawable.ic_bank_zjkyh,//张家口银行
        "CQYH" to R.drawable.ic_bank_cqyh,//重庆银行
        "CQNCSYYH" to R.drawable.ic_bank_cqncsyyh,//重庆农村商业银行
        "XAYH" to R.drawable.ic_bank_xayh,//西安银行
        "LZYH" to R.drawable.ic_bank_lzyh,//兰州银行
        "LFYH" to R.drawable.ic_bank_lfyh,//廊坊银行
        "FJNS" to R.drawable.ic_bank_fjns,//福建农商银行
        "CDNS" to R.drawable.ic_bank_cdns,//成都农商银行
        "RZYH" to R.drawable.ic_bank_rzyh,//日照银行
        "QSYH" to R.drawable.ic_bank_qsyh,//齐商银行
        "LSYH" to R.drawable.ic_bank_lsyh,//临商银行
        "GLYH" to R.drawable.ic_bank_glyh,//桂林银行
        "BJYH" to R.drawable.ic_bank_bob,//北京银行
        "BOB" to R.drawable.ic_bank_bob,//北京银行
        "TJYH" to R.drawable.ic_bank_tjyh,//天津银行
        "HBYH" to R.drawable.ic_bank_hbyh,//河北银行
        "JSYH" to R.drawable.ic_bank_jsyh,//江苏银行
        "JXYH" to R.drawable.ic_bank_jxyh,//江西银行
        "GZYH" to R.drawable.ic_bank_gzyh,//赣州银行
        "HYB" to R.drawable.ic_bank_hyb,//华一银行
        "RCC" to R.drawable.ic_bank_rcc,//农村信用社
        "SHB" to R.drawable.ic_bank_shb,// 上海银行
        "SDB" to R.drawable.ic_bank_sdb,//深圳发展银行
        "YSF" to R.drawable.ic_bank_ysf//云闪付
    )
}
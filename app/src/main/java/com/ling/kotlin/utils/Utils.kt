package com.ling.kotlin.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import com.ling.kotlin.LotteryApp
import android.util.DisplayMetrics
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ling.kotlin.R
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*


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

class DialogUtils{
    companion object{

        fun showDialog(clz: DialogFragment, @NonNull supportFragmentManager: FragmentManager, tag: String) {
            val ft = supportFragmentManager.beginTransaction()
            val fragment = supportFragmentManager.findFragmentByTag(tag)
            fragment?.let {ft.remove(it) }
            ft.add(clz, tag)
            ft.commitAllowingStateLoss()
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
    fun getWindowWidth(context: Activity?): Int {
        val metric = DisplayMetrics()
        context?.windowManager?.defaultDisplay?.getMetrics(metric)
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
    fun saveChip(chips:MutableList<Int>){
        Paper.book().write("chips",chips)
    }
    fun getChips():MutableList<Int>{
        return try {Paper.book().read("chips")}catch (e:Exception){
            mutableListOf(10,100,500)
        }
    }

    fun saveOpenSound(isOpenSound: Boolean) {
        Paper.book().write("isOpenSound", isOpenSound)
    }

    fun isOpenSound(): Boolean {
        try {
            return Paper.book().read("isOpenSound", false)
        } catch (e: Exception) {

        }

        return false
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

    fun convertTime(time:String?): String? {

        return time?.let { if (it.contains("T")) it.replace("T", " ", true) else it }
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


/**
 * 键盘相关工具类
 */
class KeyboardUtils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        /*
      避免输入法面板遮挡
      <p>在manifest.xml中activity中设置</p>
      <p>android:windowSoftInputMode="adjustPan"</p>
     */
        /**
         * 动态显示软键盘
         *
         * @param activity activity
         */
        fun showSoftInput(activity: Activity) {
            var view = activity.currentFocus
            if (view == null) view = View(activity)
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }

        /**
         * 动态显示软键盘
         *
         * @param view 视图
         */
        fun showSoftInput(view: View) {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            val imm =
                LotteryApp.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }

        /**
         * 动态隐藏软键盘
         *
         * @param activity activity
         */
        fun hideSoftInput(activity: Activity?) {
            if (activity == null)
                return
            var view = activity.currentFocus
            if (view == null) view = View(activity)
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * 动态隐藏软键盘
         *
         * @param view 视图
         */
        fun hideSoftInput(view: View) {
            val imm =
                LotteryApp.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        //显示键盘
        fun showSoftKeyboard(view: View) {
            if (view.requestFocus()) {
                val imm = LotteryApp.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        /**
         * 切换键盘显示与否状态
         */
        fun toggleSoftInput() {
            val imm =
                LotteryApp.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        /**
         * 点击屏幕空白区域隐藏软键盘
         *
         * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
         *
         * 需重写dispatchTouchEvent
         *
         * 参照以下注释代码
         */
        fun clickBlankArea2HideSoftInput() {
            Log.d("tips", "U should copy the following code.")
            /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
        }
    }
}
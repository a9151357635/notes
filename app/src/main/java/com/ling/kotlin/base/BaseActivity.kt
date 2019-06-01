package com.ling.kotlin.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ling.kotlin.utils.ActivityManager
import com.ling.kotlin.utils.ImmersionBarManager

abstract class BaseActivity : AppCompatActivity(){

    /**
     * 全屏管理
     */
    private var barManager: ImmersionBarManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        barManager = ImmersionBarManager(this)
//            barManager.transparentStatusBar();
        barManager!!.transparentStatusBar(true)
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(layoutId)
        initView()
    }
    protected abstract val layoutId:Int
    protected abstract fun initView()

    override fun onResume() {
        super.onResume()
        ActivityManager.getInstance().pushActivity(this)
    }

    override fun onDestroy() {
        if (barManager != null) {
            barManager!!.onDestroy()
            barManager = null
        }
        super.onDestroy()
    }
}
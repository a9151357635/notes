package com.ling.kotlin.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ling.kotlin.utils.ActivityManager
import com.ling.kotlin.utils.ImmersionBarManager
import kotlinx.android.synthetic.main.title_top_view.*

abstract class BaseActivity : AppCompatActivity(){

    /**
     * 全屏管理
     */
    private lateinit var barManager: ImmersionBarManager
    override fun onCreate(savedInstanceState: Bundle?) {
        barManager = ImmersionBarManager(this)
//            barManager.transparentStatusBar();
        barManager.transparentStatusBar(true)
        super.onCreate(savedInstanceState)
        supportActionBar?.let { supportActionBar!!.hide() }
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
        barManager.onDestroy()
        super.onDestroy()
    }
    /**
     * 初始化标题栏 ,默认是显示返回按钮
     * @param v
     * @param title 标题
     */
    protected fun initTitleView( title: String) {
        initTitleView(title, true)
    }


    /**
     *
     * @param v
     * @param title 标题
     * @param isBackVisible 是否显示返回按钮
     */
     fun initTitleView( title: String, isBackVisible: Boolean) {
        this.initTitleView(title, null, false, isBackVisible, null)
    }

    /**
     * 初始化标题栏的各个控件及状态的改变
     * @param v
     * @param title 标题
     * @param rightHint 最右边的字符提示
     * @param isRightVisible 是否显示右边按钮
     * @param isBackVisible 是否显示返回按钮
     * @param rightOnClick 右边控件的监听事件
     */
     fun initTitleView(
        title: String,
        rightHint: String?,
        isRightVisible: Boolean,
        isBackVisible: Boolean,
        rightOnClick: View.OnClickListener?
    ) {

        title_tv.text = title
        title_right_tv.visibility = if (isRightVisible) View.VISIBLE else View.GONE
        title_right_tv.text = rightHint
        if (rightOnClick != null) {
            title_right_tv.setOnClickListener(rightOnClick)
        }
        title_back_iv.visibility = if (isBackVisible) View.VISIBLE else View.GONE
        title_back_iv.setOnClickListener { finish() }
    }
}
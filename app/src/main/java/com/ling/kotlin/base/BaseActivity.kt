package com.ling.kotlin.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.ling.kotlin.login.LoginActivity
import com.ling.kotlin.retroft.viewmodel.BaseViewModel
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEventEventObserver
import com.ling.kotlin.utils.ActivityManager
import com.ling.kotlin.utils.ImmersionBarManager
import kotlinx.android.synthetic.main.title_top_view.*

abstract class BaseActivity : AppCompatActivity(),IBaseViewModelEventEventObserver{

    /**
     * 全屏管理
     */
    private lateinit var barManager: ImmersionBarManager
    override fun onCreate(savedInstanceState: Bundle?) {
        barManager = ImmersionBarManager(this)
//            barManager.transparentStatusBar();
        barManager.transparentStatusBar(true)
        super.onCreate(savedInstanceState)
        supportActionBar?.let { it.hide() }
        initViewModelEvent()
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
     * 初始化标题栏的各个控件及状态的改变
     * @param title 标题
     * @param rightHint 最右边的字符提示
     * @param isRightVisible 是否显示右边按钮
     * @param isBackVisible 是否显示返回按钮
     * @param rightOnClick 右边控件的监听事件
     */
    @JvmOverloads fun initTitleView(
        title: String,
        rightHint: String? = null,
        isRightVisible: Boolean = false,
        isBackVisible: Boolean = true,
        rightOnClick: View.OnClickListener? = null
    ) {

        title_tv.text = title
        title_right_tv.visibility = if (isRightVisible) View.VISIBLE else View.GONE
        title_right_tv.text = rightHint
        rightOnClick?.let {   title_right_tv.setOnClickListener(it)  }
        title_back_iv.visibility = if (isBackVisible) View.VISIBLE else View.GONE
        title_back_iv.setOnClickListener { finish() }
    }

    override fun showLoading(msg: String) {

    }
    override fun dismissLoading() {

    }
    override fun getLContext(): Context = this

    override fun getLifecycleOwner(): LifecycleOwner = this

    override fun finishView() {
        finish()
    }
    override fun tokenInvalid() {
        startActivity(LoginActivity::class.java)
    }
    override fun pop() {
    }

    fun <T :BaseViewModel> getViewModel(clz:Class<T>) = ViewModelProviders.of(this).get(clz)

    fun showDialog(clz: DialogFragment, tag: String) {
        val ft = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        fragment?.let {ft.remove(it) }
        ft.add(clz, tag)
        ft.commitAllowingStateLoss()
    }
}
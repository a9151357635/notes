package com.ling.kotlin.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.ling.kotlin.login.LoginActivity
import com.ling.kotlin.retroft.viewmodel.BaseViewModel
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEventEventObserver
import kotlinx.android.synthetic.main.title_top_view.view.*

abstract class BaseFragment : Fragment(), IBaseViewModelEventEventObserver {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId,container,false)
        initView(view)
        return view

    }
    protected abstract val layoutId:Int
    protected abstract fun initView(v:View)

    /**
     * 初始化标题栏的各个控件及状态的改变
     * @param v
     * @param title 标题
     * @param rightHint 最右边的字符提示
     * @param isRightVisible 是否显示右边按钮
     * @param isBackVisible 是否显示返回按钮
     * @param rightOnClick 右边控件的监听事件
     */

    protected @JvmOverloads fun initTitleView(v: View, title: String, rightHint: String? = null,
                                              isRightVisible: Boolean = false,
                                              isBackVisible: Boolean = true,
                                              rightOnClick: View.OnClickListener? = null) {
       v.title_tv.text = title
        v.title_right_tv.visibility = if (isRightVisible) View.VISIBLE else View.GONE
        v.title_right_tv.text = rightHint
        v.title_right_tv.text = rightHint
        rightOnClick?.let {    v.title_right_tv.setOnClickListener(it)}
        v.title_back_iv.visibility = if (isBackVisible) View.VISIBLE else View.GONE
        v.title_back_iv.setOnClickListener {  activity?.let{it.finish() }}
    }

    override fun showLoading(msg: String) {

    }
    override fun dismissLoading() {

    }
    override fun getLContext(): Context? = this.context?.let { it }

    override fun getLifecycleOwner(): LifecycleOwner = this

    override fun finishView() {
        activity?.finish()
    }
    override fun tokenInvalid() {
        startActivity(LoginActivity::class.java)
    }
    override fun pop() {
    }

    fun <T : BaseViewModel> getViewModel(activity: FragmentActivity, clz:Class<T>) = ViewModelProviders.of(activity).get(clz)

    fun <T : BaseViewModel> getViewModel( clz:Class<T>) = ViewModelProviders.of(this).get(clz)
    fun showDialog(clz: DialogFragment, tag: String) {

        val ft = childFragmentManager.beginTransaction()
        val fragment = childFragmentManager.findFragmentByTag(tag)
        fragment?.let {ft.remove(it) }
        ft.add(clz, tag)
        ft.commitAllowingStateLoss()
    }
}

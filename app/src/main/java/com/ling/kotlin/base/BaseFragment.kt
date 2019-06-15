package com.ling.kotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ling.kotlin.R
import kotlinx.android.synthetic.main.title_top_view.view.*

abstract class BaseFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId,container,false)
        initView(view)
        return view

    }
    protected abstract val layoutId:Int
    protected abstract fun initView(v:View)

    /**
     * 初始化标题栏 ,默认是显示返回按钮
     * @param v
     * @param title 标题
     */
    protected fun initTitleView(v: View, title: String) {
        initTitleView(v, title, true)
    }


    /**
     *
     * @param v
     * @param title 标题
     * @param isBackVisible 是否显示返回按钮
     */
    protected fun initTitleView(v: View, title: String, isBackVisible: Boolean) {
        this.initTitleView(v, title, null, false, isBackVisible, null)
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
    protected fun initTitleView(
        v: View,
        title: String,
        rightHint: String?,
        isRightVisible: Boolean,
        isBackVisible: Boolean,
        rightOnClick: View.OnClickListener?
    ) {
       v.title_tv.text = title
        v.title_right_tv.visibility = if (isRightVisible) View.VISIBLE else View.GONE
        v.title_right_tv.text = rightHint
        if (rightOnClick != null) {
            v.title_right_tv.setOnClickListener(rightOnClick)
        }
        v.title_back_iv.visibility = if (isBackVisible) View.VISIBLE else View.GONE
        v.title_back_iv.setOnClickListener {  activity?.let{activity!!.finish() }}
    }

}

package com.ling.kotlin.me

import android.view.View
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import kotlinx.android.synthetic.main.me_layout.view.*

class MeFragment : BaseFragment(){
    override val layoutId: Int
        get() = R.layout.me_layout

    override fun initView(v: View) {
//        v.me.setText("个人中心")
//        me.setText("我是个人中心")
    }

}
package com.ling.kotlin.pay

import android.view.View
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import kotlinx.android.synthetic.main.pay_layout.view.*

class PayFragment : BaseFragment(){
    override val layoutId: Int
        get() = R.layout.pay_layout

    override fun initView(v: View) {
//        v.pay.setText("支付中心")
    }

}
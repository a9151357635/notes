package com.ling.kotlin.wallet

import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity

/**
 * 支付页面
 */
class PayActivity : BaseActivity(){
    override val layoutId: Int = R.layout.wallet_pay_layout

    override fun initView() {
        initTitleView("充值")
    }
}
package com.ling.kotlin.wallet

import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity

/**
 * 提现页面
 */
class WithdrawActivity :BaseActivity(){
    override val layoutId: Int = R.layout.wallet_withdraw_layout

    override fun initView() {
        initTitleView("提现")

    }
}
package com.ling.kotlin.wallet

import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.utils.BankUtils
import kotlinx.android.synthetic.main.wallet_withdraw_layout.*

/**
 * 提现页面
 */
class WithdrawActivity :BaseActivity(){
    override val layoutId: Int = R.layout.wallet_withdraw_layout

    override fun initView() {
        initTitleView("提现")
        BankUtils.bankMap["ABC"]?.let { withdraw_bank_iv.setImageResource(it) }
    }
}
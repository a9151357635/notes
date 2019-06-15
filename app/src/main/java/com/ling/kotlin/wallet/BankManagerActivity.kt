package com.ling.kotlin.wallet

import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity

class BankManagerActivity : BaseActivity(){
    override val layoutId: Int = R.layout.wallet_bank_manager_layout
    override fun initView() {
        initTitleView("银行卡管理")
    }
}
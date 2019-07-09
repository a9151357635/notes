package com.ling.kotlin.wallet

import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity

class ConvertActivity : BaseActivity(){
    override val layoutId: Int = R.layout.wallet_convert_layout

    override fun initView() {
        initTitleView("金额转换")
    }
}
package com.ling.kotlin.wallet

import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity

/**
 * 钱包订单详情
 */
class WalletDetailActivity : BaseActivity() {
    override val layoutId: Int = R.layout.wallet_detail_layout

    override fun initView() {
        initTitleView("订单详情")
    }
}

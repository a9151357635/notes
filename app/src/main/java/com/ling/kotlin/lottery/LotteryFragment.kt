package com.ling.kotlin.lottery

import android.view.View
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import kotlinx.android.synthetic.main.lottery_layout.view.*

class LotteryFragment: BaseFragment(){
    override val layoutId: Int
        get() = R.layout.lottery_layout

    override fun initView(v: View) {
//        v.lottery.setText("彩票中心")
    }

}
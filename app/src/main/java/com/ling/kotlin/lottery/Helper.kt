package com.ling.kotlin.lottery

import android.view.View
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.base.BaseFragment

/**
 * 助手页面
 */
class HelperActivity(override val layoutId: Int = R.layout.lottery_helper_home_layout):BaseActivity(){
    override fun initView() {

    }

}
class HistoryFragment(override val layoutId: Int= R.layout.lottery_helper_history_layout):BaseFragment(){
    override fun initView(v: View) {

    }

}
class ChartFragment(override val layoutId: Int= R.layout.lottery_helper_chart_layout):BaseFragment(){
    override fun initView(v: View) {

    }
}
class DescFragment(override val layoutId: Int= R.layout.lottery_helper_desc_layout) :BaseFragment(){
    override fun initView(v: View) {

    }

}
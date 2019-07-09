package com.ling.kotlin.me

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import kotlinx.android.synthetic.main.me_lottery_history_layout.*

class LotteryHistoryActivity : BaseActivity(){
    override val layoutId: Int = R.layout.me_lottery_history_layout

    override fun initView() {
        initTitleView("开奖历史")
        val host: NavHostFragment = historyHost as NavHostFragment??:return
        val graph = host.navController.graph
        graph.startDestination = changeResId()
        host.navController.graph = graph
        historyBV.setupWithNavController(host.navController)
        //取消导航栏图标着色
        historyBV.itemIconTintList = null
    }

    private fun changeResId():Int{
        intent.extras?.let {
            return when(it.getString("type")){
                "0" -> R.id.wait
                "1" -> R.id.win
                "2" -> R.id.nowin
                else -> R.id.wait
            }
        }
        return R.id.wait
    }
}
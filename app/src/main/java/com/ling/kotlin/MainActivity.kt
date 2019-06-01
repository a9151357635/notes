package com.ling.kotlin

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ling.kotlin.base.BaseActivity

import kotlinx.android.synthetic.main.main_layout.*

class MainActivity : BaseActivity() {

    override fun initView() {
        val host: NavHostFragment = host as NavHostFragment??:return
        bottomView.setupWithNavController(host.navController)

    }

    override val layoutId: Int
        get() = R.layout.main_layout
}

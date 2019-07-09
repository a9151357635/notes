package com.ling.kotlin

import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.base.NoticeDialog
import com.ling.kotlin.common.CommonViewModel
import kotlinx.android.synthetic.main.main_layout.*

class MainActivity( override val layoutId: Int= R.layout.main_layout) : BaseActivity() {

    private lateinit var viewMode:CommonViewModel

    override fun initView() {
        val host: NavHostFragment = host as NavHostFragment??:return
        bottomView.setupWithNavController(host.navController)
        //取消导航栏图标着色
        bottomView.itemIconTintList = null
        setUpView()
    }

    private fun setUpView() {
        viewMode = getViewModel(CommonViewModel::class.java)
        viewMode.requestBannerRemote(true).observe(this, Observer { })
        viewMode.requestNoticeRemote(true).observe(this, Observer {
           showDialog(NoticeDialog().apply { arguments =  bundleOf("notices" to it)},"noticeDialog")
        })
    }
}

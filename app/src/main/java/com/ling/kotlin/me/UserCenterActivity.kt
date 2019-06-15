package com.ling.kotlin.me

import android.content.Intent
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.login.LoginActivity
import kotlinx.android.synthetic.main.me_user_center_layout.*

/**
 * 用户管理中心
 */
class UserCenterActivity : BaseActivity(){

    override val layoutId: Int= R.layout.me_user_center_layout

    override fun initView() {
        initTitleView("账号管理中心")
        me_logout_tv.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
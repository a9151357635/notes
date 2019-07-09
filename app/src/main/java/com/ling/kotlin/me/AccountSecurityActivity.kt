package com.ling.kotlin.me

import android.content.Intent
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.login.LoginActivity
import kotlinx.android.synthetic.main.me_account_security_layout.*

/**
 * 用户管理中心
 */
class AccountSecurityActivity : BaseActivity(){

    override val layoutId: Int= R.layout.me_account_security_layout

    override fun initView() {
        initTitleView("账号与安全")
        me_logout_tv.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
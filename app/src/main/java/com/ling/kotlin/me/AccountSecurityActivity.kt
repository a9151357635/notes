package com.ling.kotlin.me

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.common.UserInfoLiveData
import com.ling.kotlin.login.LoginActivity
import kotlinx.android.synthetic.main.me_account_security_layout.*

/**
 * 用户管理中心
 */
class AccountSecurityActivity( override val layoutId: Int= R.layout.me_account_security_layout) : BaseActivity(){

    override fun initView() {
        initTitleView("账号与安全")
        me_logout_tv.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        UserInfoLiveData.observe(this, Observer {
            me_user_detail_icon_iv.setImageURI(Uri.parse(it.headPhoto),null)
            me_user_detail_username_tv.text = it.userName
            me_user_detail_zh_username_tv.text = it.realName
            me_user_detail_iphone_tv.text = it.phone
        })
    }
}
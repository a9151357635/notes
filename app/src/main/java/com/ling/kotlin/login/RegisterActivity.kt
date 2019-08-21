package com.ling.kotlin.login

import androidx.lifecycle.Observer
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.login.repository.LoginViewModel
import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.utils.VideoUtils
import kotlinx.android.synthetic.main.register_layout.*


class RegisterActivity(override val layoutId: Int = R.layout.register_layout) :BaseActivity(){

    private lateinit var viewModel: LoginViewModel
    override fun initView() {
        initTitleView("注册页面")
        viewModel = getViewModel(LoginViewModel::class.java)
        register_create_account_tv.setOnClickListener {
            register()
        }
    }

    override fun onResume() {
        super.onResume()
        VideoUtils.startVideo(this, R.raw.login_video, register_video)
    }

    private fun register(){
        val account = register_account_tl.editText?.text.toString()
        val createPwd = register_create_pwd_tl.editText?.text.toString()
        val rePwd = register_rep_pwd_tl.editText?.text.toString()
        val realName = register_china_name_tl.editText?.text.toString()
        val phone = register_phone_tl.editText?.text.toString()
        when {
            account.isNull() -> {
                showToast(getString(R.string.app_register_error_account_hint))
                return
            }
            account.isNull() -> {
                showToast(getString(R.string.app_register_error_pwd_hint))
                return
            }
            createPwd.length < 6 -> {
                showToast(getString(R.string.app_register_error_pwd_length_hint))
                return
            }
            account.isNull() -> {
                showToast(getString(R.string.app_register_error_repwd_hint))
                return
            }
            account.isNull() -> {
                showToast(getString(R.string.app_register_error_realname_hint))
                return
            }
            account.isNull() -> {
                showToast(getString(R.string.app_register_error_phone_hint))
                return
            }
            createPwd != rePwd -> {
                showToast(getString(R.string.app_register_error_same_pwd_hint))
                return
            }
            !AppUtils.matcherAccount(account) -> {
                showToast(getString(R.string.app_register_error_format_account_hint))
                return
            }
            AppUtils.isContainChinese(createPwd) -> {
                showToast(getString(R.string.app_register_error_format_pwd_hint))
                return
            }
            !AppUtils.matcherZh(realName) -> {
                showToast(getString(R.string.app_register_error_format_realname_hint))
                return
            }
            !AppUtils.matcherIphoneNumber(phone) -> {
                showToast(getString(R.string.app_register_error_format_phone_hint))
                return
            }
            else -> {
                val map = mapOf(
                    "companyPlatformId" to 0,
                    "confirmedPassword" to createPwd,
                    "email" to "519094873@qq.com",
                    "introducer" to "Ling",
                    "password" to "123456",
                    "payPwd" to "1234",
                    "phone" to phone,
                    "realName" to realName,
                    "userName" to account
                )

                viewModel.register(map).observe(this, Observer {
                    showToast(msg = it.toString())
                    finish()
                })
            }
        }

    }

    /**
     * 扩展函数
     */
    private fun String?.isNull():Boolean{
        return this == "null" ||  this =="" || this == null
    }
}
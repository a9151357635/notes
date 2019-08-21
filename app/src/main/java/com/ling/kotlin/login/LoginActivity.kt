package com.ling.kotlin.login

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ling.kotlin.MainActivity
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.login.repository.LoginViewModel
import com.ling.kotlin.utils.VideoUtils
import kotlinx.android.synthetic.main.login_layout.*

/**
 * 登陆页面
 */
class LoginActivity (override val layoutId: Int = R.layout.login_layout): BaseActivity(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel

    override fun initView() {
        login_close_iv.setOnClickListener(this)
        login_btn.setOnClickListener(this)
        login_free_btn.setOnClickListener(this)
        login_register_btn.setOnClickListener(this)
        viewModel = getViewModel(LoginViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        VideoUtils.startVideo(this, R.raw.login_video, login_video)
    }
    override fun onPause() {
        super.onPause()
        login_video.pause()
    }
    override fun onDestroy() {
        login_video.stopPlayback()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_close_iv -> gotoMainActivity()
            R.id.login_free_btn -> gotoMainActivity()
            R.id.login_btn -> login()
            R.id.login_register_btn -> startActivity(RegisterActivity::class.java)
        }
    }

    private fun login(){
        val account = login_account_tl.editText?.text.toString()
        val pwd = login_password_tl.editText?.text.toString()
        viewModel.login(mapOf("userName" to account,"password" to pwd)).observe(this, Observer {
            gotoMainActivity()
        })
    }

    private fun gotoMainActivity(){
        val  intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finishView()
    }
}
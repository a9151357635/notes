package com.ling.kotlin.login

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.ling.kotlin.MainActivity
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.utils.VideoUtils
import kotlinx.android.synthetic.main.login_layout.*

/**
 * 登陆页面
 */
class LoginActivity : BaseActivity(), View.OnClickListener {

    override val layoutId: Int = R.layout.login_layout

    override fun initView() {
        login_close_iv.setOnClickListener(this)
        login_btn.setOnClickListener(this)
        login_free_btn.setOnClickListener(this)

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
        }
    }

    private fun gotoMainActivity(){
        val  intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    private fun login(){
       Toast.makeText(this,"敬请期待",Toast.LENGTH_LONG).show()
    }
}
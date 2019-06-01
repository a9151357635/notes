package com.ling.kotlin.server

import android.view.View
import android.widget.Toast
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import kotlinx.android.synthetic.main.server_layout.view.*

class ServerFragment:BaseFragment(), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.server_layout
    override fun initView(v: View) {
//        v.toolbar.setTitle("服务中心")
//        v.server_online_tv.setOnClickListener(this)
//        v.server_qq_tv.setOnClickListener(this)
//        v.server_weixin_tv.setOnClickListener(this)
    }
    override fun onClick(v: View) {
//        when(v.id){
//            R.id.server_online_tv -> Toast.makeText(context,"在线客服",Toast.LENGTH_LONG ).show()
//            R.id.server_qq_tv -> Toast.makeText(context,"QQ客服",Toast.LENGTH_LONG ).show()
//            R.id.server_weixin_tv -> Toast.makeText(context,"微信客服",Toast.LENGTH_LONG ).show()
//        }
    }

}
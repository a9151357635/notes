package com.ling.kotlin.chess

import android.view.View
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import kotlinx.android.synthetic.main.chess_layout.view.*

class ChessFragment(override val layoutId:Int = R.layout.chess_layout):BaseFragment(){

    override fun initView(v: View) {
        v.chess.text = "棋牌中心"
    }

}
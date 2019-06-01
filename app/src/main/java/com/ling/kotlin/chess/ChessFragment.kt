package com.ling.kotlin.chess

import android.view.View
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import kotlinx.android.synthetic.main.chess_layout.view.*

class ChessFragment:BaseFragment(){
    override val layoutId: Int
        get() = R.layout.chess_layout

    override fun initView(v: View) {
        v.chess.setText("棋牌中心")
    }

}
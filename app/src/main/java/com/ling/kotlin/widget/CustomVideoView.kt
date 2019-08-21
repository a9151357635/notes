package com.ling.kotlin.widget

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.VideoView

class CustomVideoView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : VideoView(context, attrs,defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //重新计算高度
        val width = View.getDefaultSize(0, widthMeasureSpec)
        val height = View.getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }
}
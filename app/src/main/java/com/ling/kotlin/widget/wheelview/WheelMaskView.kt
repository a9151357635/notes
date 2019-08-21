package com.ling.kotlin.widget.wheelview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.ling.kotlin.R

/**
 * 分割线视图
 */
class WheelMaskView: View {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var lineColor = -0x70ffff01

    private var updateTop = 0
    private var updateBottom = 0
    constructor(context: Context) : super(context) {
        initAttr(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs, defStyleAttr)
    }
    fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WheelMaskView, defStyleAttr, 0)
        lineColor = a.getColor(R.styleable.WheelMaskView_wheelMaskLineColor, -0x70ffff01)
        a.recycle()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
    }

    fun updateMask(heightCount: Int, itemHeight: Int) {
        if (heightCount > 0) {
            val centerIndex = heightCount / 2
            updateTop = centerIndex * itemHeight
            updateBottom = updateTop + itemHeight
        } else {
            updateTop = 0
            updateBottom = 0
        }
        invalidate()
    }

    fun setLineColor(@ColorInt lineColor: Int) {
        this.lineColor = lineColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (top > 0 && bottom > 0) {
            paint.color = lineColor
            canvas.drawLine(0f, updateTop.toFloat(), width.toFloat(), updateTop.toFloat(), paint)
            canvas.drawLine(0f, updateBottom.toFloat(), width.toFloat(), updateBottom.toFloat(), paint)
        }
    }
}

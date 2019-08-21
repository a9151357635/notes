package com.regus.game.widget.wheelview

import android.content.Context

import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.ling.kotlin.widget.wheelview.IWheel
import com.ling.kotlin.widget.wheelview.IWheelViewSetting
import com.ling.kotlin.widget.wheelview.WheelMaskView

/***
 * Wheel view with selected mask.
 */
class WheelItemView : FrameLayout, IWheelViewSetting {

    var wheelView: WheelView? = null
        private set
    var wheelMaskView: WheelMaskView? = null
        private set

    override var selectedIndex: Int
        get() = wheelView?.selectedIndex?:0
        set(targetIndexPosition) = setSelectedIndex(targetIndexPosition, true)

    override val isScrolling: Boolean
        get() = wheelView?.isScrolling?:false

    constructor(context: Context) : super(context) {
        initAttr(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs, defStyleAttr)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        wheelView = WheelView(context)
        wheelView?.initAttr(context, attrs, defStyleAttr)
        wheelMaskView = WheelMaskView(context)
        wheelMaskView?.initAttr(context, attrs, defStyleAttr)
        addView(wheelView,LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        addView(wheelMaskView,LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val params = wheelMaskView?.layoutParams
        wheelView?.let {
            params?.height = it.measuredHeight
            wheelMaskView?.layoutParams = params
            wheelMaskView?.updateMask(it.getShowCount(), it.getItemHeight())
        }
    }

    override fun setTextSize(textSize: Float) {
        wheelView?.setTextSize(textSize)
    }

    override fun setTextColor(@ColorInt textColor: Int) {
        wheelView?.setTextColor(textColor)
    }

    override fun setShowCount(showCount: Int) {
        wheelView?.setShowCount(showCount)
    }

    override fun setTotalOffsetX(totalOffsetX: Int) {
        wheelView?.setTotalOffsetX(totalOffsetX)
    }

    override fun setItemVerticalSpace(itemVerticalSpace: Int) {
        wheelView?.setItemVerticalSpace(itemVerticalSpace)
    }

    override fun setItems(items: List<IWheel>?) {
        wheelView?.setItems(items)
    }

    override fun setSelectedIndex(targetIndexPosition: Int, withAnimation: Boolean) {
        wheelView?.setSelectedIndex(targetIndexPosition, withAnimation)
    }

    override fun setOnSelectedListener(onSelectedListener: WheelView.OnSelectedListener) {
        wheelView?.setOnSelectedListener(onSelectedListener)
    }

    fun setMaskLineColor(@ColorInt color: Int) {
        wheelMaskView?.setLineColor(color)
    }
}

package com.ling.kotlin.widget.wheelview

import androidx.annotation.ColorInt
import com.regus.game.widget.wheelview.WheelView

interface IWheel {
    val showText: String
}

/**
 * IWheelViewSetting
 */
interface IWheelViewSetting {

    var selectedIndex: Int

    val isScrolling: Boolean

    fun setTextSize(textSize: Float)

    fun setTextColor(@ColorInt textColor: Int)

    fun setShowCount(showCount: Int)

    fun setTotalOffsetX(totalOffsetX: Int)

    fun setItemVerticalSpace(itemVerticalSpace: Int)

    fun setItems(items: List<IWheel>?)

    fun setSelectedIndex(targetIndexPosition: Int, withAnimation: Boolean)

    fun setOnSelectedListener(onSelectedListener: WheelView.OnSelectedListener)
}
package com.ling.kotlin.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Ling on 2018/4/30.
 */

class GridItemDecoration
/**
 * 默认分割线：高度为2px，颜色为灰色
 *
 * @param context
 */
    (context: Context) : RecyclerView.ItemDecoration() {
    private var mPaint: Paint? =null
    private var mDivider: Drawable? = null
    private var mDividerHeight = 2//分割线高度，默认为1px

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId  分割线图片
     */
    constructor(context: Context, drawableId: Int) : this(context) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider?.intrinsicHeight?:0
    }

    /**
     * 自定义分割线
     *
     * @param context
     *
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, mDividerHeight: Int, dividerColor: Int) : this(context) {
        this.mDividerHeight = mDividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = ContextCompat.getColor(context, dividerColor)
        mPaint?.style = Paint.Style.FILL
    }

    /**
     * 基本操作是留出分割线位置
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, 0, 0, mDividerHeight)
        var bottom = mDivider?.intrinsicHeight
        var right = mDivider?.intrinsicWidth

        //如果是最后一列，留出位置
        if (isLastCol(view, parent)) {
            right = 0
        }
//        //如果是最后一行，留出位置
//        if (isLastRow(view, parent)) {
//            bottom = 0
//        }

        outRect.bottom = bottom?:0
        outRect.right = right?:0
    }

    /**
     * 是否是最后一行
     * 当前位置+1 > (行数-1)*列数
     *
     * @return
     */
    private fun isLastRow(view: View, parent: RecyclerView): Boolean {
        //获取当前位置
        val currentPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        //获取列数
        val mSpanCount = getSpanCount(parent)
        //获取行数 =  总的条目/列数 如果能除尽则为行数，如果除不尽，则为结果+1
        var rowNum = 0
        parent?.adapter?.let {
             rowNum = if(it.itemCount / mSpanCount ==0) it.itemCount / mSpanCount else it.itemCount / mSpanCount + 1
        }
        return currentPosition + 1 > (rowNum - 1) * mSpanCount
    }

    /**
     * 是否是最后一列
     * （当前位置+1）% 列数 ==0，判断是否为最后一列
     *
     * @return
     */
    private fun isLastCol(view: View, parent: RecyclerView): Boolean {
        //获取当前位置
        val currentPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        //获取列数
        val mSpanCount = getSpanCount(parent)
        return (currentPosition + 1) % mSpanCount == 0
    }

    /**
     * 获取列数
     * 如果是GridView，就获取列数，如果是ListView，列数就是1
     *
     * @param parent
     * @return
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        val mLayoutManager = parent.layoutManager
        if (mLayoutManager is GridLayoutManager) {
            val mGridLayoutManager = mLayoutManager as? GridLayoutManager
            //获取列数
            return mGridLayoutManager?.spanCount?:0
        }

        return 1
    }

    /**
     * 绘制分割线
     *
     * @param canvas
     * @param parent
     * @param state
     */
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        //绘制水平方向
        parent.forEach {
            val mLayoutParams = it.layoutParams as RecyclerView.LayoutParams
            val left = it.left - mLayoutParams.leftMargin
            val right = it.right + mDivider?.intrinsicWidth!!+ mLayoutParams.rightMargin
            val top = it.bottom + mLayoutParams.bottomMargin
            val bottom = top + mDividerHeight

            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
            mPaint?.let { paint ->
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
            }
        }
        //绘制垂直方向
        parent.forEach {
            val mLayoutParams = it.layoutParams as RecyclerView.LayoutParams
            val left = it.right + mLayoutParams.rightMargin
            val right = left + mDividerHeight
            val top = it.top/* - mLayoutParams.topMargin + 20*/
            val bottom = it.bottom + mLayoutParams.bottomMargin/*-20*/

            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
            mPaint?.let {paint ->
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
            }
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}

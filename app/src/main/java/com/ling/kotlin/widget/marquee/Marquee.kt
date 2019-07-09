package com.ling.kotlin.widget.marquee

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import com.ling.kotlin.R
import com.ling.kotlin.utils.DisplayUtils
import java.util.*


/**
 * 负责解析提供数据及事件监听
 * @param <T>
 * @param <E>
</E></T> */
abstract class MarqueeFactory<T : View, E>(val mContext: Context) : Observable() {
    private var mViews: MutableList<T>? = null
    private lateinit var dataList: MutableList<E>
    private var mMarqueeView: MarqueeView<*, *>? = null

    private val isAttachedToMarqueeView: Boolean = this.mMarqueeView != null

    protected abstract fun generateMarqueeItemView(data: E): T

    fun getViews(): MutableList<T>  = mViews ?: mutableListOf()

    fun setNewDataList(dataList: MutableList<E>?) {
        if (dataList == null) {
            return
        }
        this.dataList = dataList
        if(mViews != null){
            mViews?.clear()
        }else{
            mViews = mutableListOf()
        }
        for (e in dataList) {
            val mView = generateMarqueeItemView(e)
            mViews?.add(mView)
        }
        notifyDataChanged()
    }

    fun getDataList(): List<E>  = dataList

    fun attachedToMarqueeView(marqueeView: MarqueeView<*, *>) {
        if (!isAttachedToMarqueeView) {
            this.mMarqueeView = marqueeView
            this.addObserver(mMarqueeView)
            return
        }
        throw IllegalStateException(
            String.format(
                "The %s has been attached to the %s",
                toString(),
                mMarqueeView?.toString()
            )
        )
    }

    private fun notifyDataChanged() {
        if (isAttachedToMarqueeView) {
            setChanged()
            notifyObservers(COMMAND_UPDATE_DATA)
        }
    }

    companion object {
        const val COMMAND_UPDATE_DATA = "UPDATE_DATA"
    }
}


/**
 * 基本属性视图
 *  注意：interval 必须大于 animDuration，否则视图将会重叠
 *
 */
open class MarqueeView<T : View, E> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewFlipper(context, attrs), Observer {
    protected var factory: MarqueeFactory<T, E>? = null
    private var onItemClickListener: OnItemClickListener<T, E>? = null
    private var isJustOnceFlag = true

    private val onClickListener = OnClickListener {

        if (factory == null || factory?.getDataList().isNullOrEmpty()|| childCount == 0) {
            onItemClickListener?.onItemClickListener(null as T, null as E, -1)
            return@OnClickListener
        }
        val displayedChild = displayedChild
        val data = factory?.getDataList()?.get(displayedChild)
        onItemClickListener?.onItemClickListener(currentView as T, data as E, displayedChild)
    }

    init {
        init(attrs)
    }

    private fun init(attributeSet: AttributeSet?) {
        if (inAnimation == null || outAnimation == null) {
            setInAnimation(context, R.anim.in_from_down_amin)
            setOutAnimation(context, R.anim.out_to_up_amin)
        }
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.MarqueeView)
        if (a.hasValue(R.styleable.MarqueeView_marqueeAnimDuration)) {
            val animDuration = a.getInt(R.styleable.MarqueeView_marqueeAnimDuration, -1)
            inAnimation.duration = animDuration.toLong()
            outAnimation.duration = animDuration.toLong()
        }
        a.recycle()
        setOnClickListener(onClickListener)
    }

    fun setAnimDuration(animDuration: Long) {
        inAnimation?.let { it.duration = animDuration }
        outAnimation?.let {  it.duration = animDuration  }
    }

    fun setInAndOutAnim(inAnimation: Animation, outAnimation: Animation) {
        setInAnimation(inAnimation)
        setOutAnimation(outAnimation)
    }

    fun setInAndOutAnim(@AnimRes inResId: Int, @AnimRes outResId: Int) {
        setInAnimation(context, inResId)
        setOutAnimation(context, outResId)
    }

    fun setMarqueeFactory(factory: MarqueeFactory<T, E>) {
        this.factory = factory
        factory.attachedToMarqueeView(this)
        refreshChildViews()
    }

    protected open fun refreshChildViews() {
        if (childCount > 0) {
            removeAllViews()
        }
        val views = factory?.getViews()
        views?.iterator()?.forEach { view ->
            addView(view)
        }
    }

    override fun update(o: Observable, arg: Any?) {
        if (arg == null) return
        if (MarqueeFactory.COMMAND_UPDATE_DATA == arg.toString()) {
            val animation = inAnimation
            if (animation != null && animation.hasStarted()) {
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        refreshChildViews()
                        animation?.setAnimationListener(null)
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                return
            }
            refreshChildViews()
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (isJustOnceFlag) {
            super.setOnClickListener(l)
            isJustOnceFlag = false
        } else {
            throw UnsupportedOperationException("The setOnClickListener method is not supported,please use setOnItemClickListener method.")
        }
    }

    fun setOnItemClickListener(onItemChickListener: OnItemClickListener<T, E>) {
        this.onItemClickListener = onItemChickListener
    }
}

/**
 * 简单的跑马灯效果
 * @param <E>
</E> */
class SimpleMarqueeView<E> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    MarqueeView<TextView, E>(context, attrs) {
    private var smvTextColor: ColorStateList? = null
    private var smvTextSize = 15f
    private var smvTextGravity = Gravity.NO_GRAVITY
    private var smvTextSingleLine = false
    private var smvTextEllipsize: TextUtils.TruncateAt? = null

    init {
        var ellipsize = -1
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.SimpleMarqueeView)
            smvTextColor = a.getColorStateList(R.styleable.SimpleMarqueeView_smvTextColor)
            if (a.hasValue(R.styleable.SimpleMarqueeView_smvTextSize)) {
                smvTextSize = a.getDimension(R.styleable.SimpleMarqueeView_smvTextSize, smvTextSize)
                smvTextSize = DisplayUtils.px2Sp(context, smvTextSize).toFloat()
            }
            smvTextGravity = a.getInt(R.styleable.SimpleMarqueeView_smvTextGravity, smvTextGravity)
            smvTextSingleLine = a.getBoolean(R.styleable.SimpleMarqueeView_smvTextSingleLine, smvTextSingleLine)
            ellipsize = a.getInt(R.styleable.SimpleMarqueeView_smvTextEllipsize, ellipsize)
            a.recycle()
        }
        if (smvTextSingleLine && ellipsize < 0) {
            ellipsize = 3
        }

        when (ellipsize) {
            1 -> smvTextEllipsize = TextUtils.TruncateAt.START
            2 -> smvTextEllipsize = TextUtils.TruncateAt.MARQUEE
            3 -> smvTextEllipsize = TextUtils.TruncateAt.END
        }
    }

    override fun refreshChildViews() {
        super.refreshChildViews()
        val viewList = factory?.getViews()
        viewList?.iterator()?.forEach { view ->
            view.textSize = smvTextSize
            view.gravity = smvTextGravity
            if (smvTextColor != null) {
                view.setTextColor(smvTextColor)
            }
            view.setSingleLine(smvTextSingleLine)
            view.ellipsize = smvTextEllipsize
        }
    }

    fun setTextSize(textSize: Float) {
        this.smvTextSize = textSize
        factory?.getViews()?.iterator()?.forEach {
            it.textSize = textSize
        }
    }

    fun setTextColor(@ColorInt color: Int) {
        setTextColor(ColorStateList.valueOf(color))
    }

    private fun setTextColor(colorStateList: ColorStateList) {
        this.smvTextColor = colorStateList
        factory?.getViews()?.iterator()?.forEach {
            it.setTextColor(smvTextColor)
        }
    }

    fun setTextGravity(gravity: Int) {
        this.smvTextGravity = gravity
        factory?.getViews()?.iterator()?.forEach {
            it.gravity = smvTextGravity
        }
    }

    fun setTextSingleLine(singleLine: Boolean) {
        this.smvTextSingleLine = singleLine
        factory?.getViews()?.iterator()?.forEach {
            it.setSingleLine(smvTextSingleLine)
        }
    }

    fun setTextEllipsize(where: TextUtils.TruncateAt) {
        if (where == TextUtils.TruncateAt.MARQUEE) {
            throw UnsupportedOperationException("The type MARQUEE is not supported!")
        }
        this.smvTextEllipsize = where
        factory?.getViews()?.iterator()?.forEach {
            it.ellipsize = where
        }
    }
}

class SimpleMF<E : CharSequence>(mContext: Context) : MarqueeFactory<TextView, E>(mContext) {
    override fun generateMarqueeItemView(data: E): TextView {
        val mView = TextView(mContext)
        mView.text = data
        return mView
    }
}

interface OnItemClickListener<V : View, E> {
    fun onItemClickListener(view: V, data: E, position: Int)
}
package com.regus.game.widget.wheelview

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator
import android.widget.OverScroller
import androidx.annotation.ColorInt
import com.ling.kotlin.R
import com.ling.kotlin.widget.wheelview.IWheel
import com.ling.kotlin.widget.wheelview.IWheelViewSetting
import com.ling.kotlin.widget.wheelview.WheelItem
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/***
 * 滚动基本视图基类
 */
class WheelView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs,defStyle) ,
    IWheelViewSetting {

    private val TAG = "WheelView"
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera()
    private val newMatrix = Matrix()
    private var textBaseLine = 0f

    private var items: List<IWheel>? = null
    /**
     * The color of show text.
     */
    private var textColor = Color.BLACK
    /**
     * The size of showing text.
     * Default value is 14dp.
     */
    private var textSize = 0.0f
    /**
     * The offset pixel from x coordination.
     * <br></br>text align `right` with a positive value
     * <br></br>text align `center` with 0 value
     * <br></br>text align `left` with a negative value
     */
    private var totalOffsetX = 0
    /**
     * the average pixel length of show text.
     */
    private var averageShowTextLength = 0f
    /**
     * The most showing item count.
     * use it to measure view's height
     */
    private var showCount = 5
    /**
     * The most draw item count.
     */
    private var drawCount = showCount + 2
    private val defaultRectArray = mutableListOf<Rect>()
    private val drawRectArray= mutableListOf<Rect>()
    private var offsetY = 0
    private var totalMoveY = 0
    private var wheelRotationX = 0f
    private var velocityUnits = 0
    /**
     * the space width of two items
     */
    private var itemVerticalSpace = 0
    /**
     * the height of every item
     */
    private var itemHeight = 0
    private var lastX = 0.0f
    private var lastY = 0.0f
    private val calculateResult = IntArray(2)//for saving the calculate result.
    private var selectedPosition = 0//the selected index position
    private var onSelectedListener: OnSelectedListener? = null
    private var animator: ValueAnimator? = null
    private var isAnimScrolling = false
    private var isAnimatorCanceledForwardly = false//whether cancel auto scroll animation forwardly
    private val rectF = RectF()
    private var touchDownTimeStamp: Long = 0

    //about fling action
    private var mMinimumVelocity: Int = 0
    private var mMaximumVelocity: Int = 0
    private var scaledTouchSlop: Int = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var mOverScroller: OverScroller? = null
    private var flingDirection = 0//-1向上、1向下

    private val itemCount: Int
        get() =items?.size?:0

    private val isEmpty: Boolean
        get() = itemCount == 0

    init {
        initAttr(context, attrs, defStyle)
    }

    fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        mOverScroller = OverScroller(context)
        val viewConfiguration = ViewConfiguration.get(context)
        mMinimumVelocity = viewConfiguration.scaledMinimumFlingVelocity
        mMaximumVelocity = viewConfiguration.scaledMaximumFlingVelocity
        scaledTouchSlop = viewConfiguration.scaledTouchSlop

        val a = context.obtainStyledAttributes(attrs, R.styleable.WheelView, defStyleAttr, 0)
        val defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, resources.displayMetrics)
        textColor = a.getColor(R.styleable.WheelView_wheelTextColor, -0xcccccd)
        textSize = a.getDimension(R.styleable.WheelView_wheelTextSize, defaultTextSize)
        showCount = a.getInt(R.styleable.WheelView_wheelShowCount, 5)
        totalOffsetX = a.getDimensionPixelSize(R.styleable.WheelView_wheelTotalOffsetX, 0)
        itemVerticalSpace = a.getDimensionPixelSize(R.styleable.WheelView_wheelItemVerticalSpace, 32)
        wheelRotationX = a.getFloat(R.styleable.WheelView_wheelRotationX, DEFAULT_ROTATION_X)
        velocityUnits = a.getInteger(R.styleable.WheelView_wheelRotationX, DEFAULT_VELOCITY_UNITS)
        if (velocityUnits < 0) {
            velocityUnits = abs(velocityUnits)
        }
        a.recycle()
        initConfig()
        if (isInEditMode) {
            val items = ArrayList<IWheel>()
            for (i in 0..7) {
                items.add(WheelItem("菜单选项" + if (i < 10) "0$i" else i.toString()))
            }
            setItems(items)
        }
    }

    private fun initConfig() {
        textPaint.color = textColor
        textPaint.textSize = textSize
        val fontMetrics = textPaint.fontMetrics
        val testText = "菜单选项"
        val rect = Rect()
        textPaint.getTextBounds(testText, 0, testText.length, rect)
        itemHeight = rect.height() + itemVerticalSpace
        textBaseLine = -itemHeight / 2.0f + (itemHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
        showCount = if (showCount < 5) 5 else showCount
        if (showCount % 2 == 0) {
            showCount++
        }
        drawCount = showCount + 2
        defaultRectArray.clear()
        drawRectArray.clear()
        for (i in 0 until drawCount) {
            defaultRectArray.add(i,Rect())
            drawRectArray.add(i,Rect())
        }
    }

    override fun setItems(items: List<IWheel>?) {
        this.items = items
        if (!isEmpty) {
            averageShowTextLength = calAverageShowTextLength()
            invalidate()
        }
    }
    override var selectedIndex: Int
        get() = selectedPosition
        set(value) { setSelectedIndex(value, true)}
    override val isScrolling: Boolean
        get() =  isAnimScrolling

    override fun setTextSize(textSize: Float) {
        this.textSize = textSize
        initConfig()
        requestLayout()
    }

    override fun setTextColor(@ColorInt textColor: Int) {
        this.textColor = textColor
        textPaint.color = textColor
        invalidate()
    }

    override fun setShowCount(showCount: Int) {
        this.showCount = showCount
        initConfig()
        requestLayout()
    }

    override fun setTotalOffsetX(totalOffsetX: Int) {
        this.totalOffsetX = totalOffsetX
        invalidate()
    }

    override fun setItemVerticalSpace(itemVerticalSpace: Int) {
        this.itemVerticalSpace = itemVerticalSpace
        initConfig()
        requestLayout()
    }

    override fun setOnSelectedListener(onSelectedListener: OnSelectedListener) {
        this.onSelectedListener = onSelectedListener
    }

    /**
     * Scroll to fixed index position.
     *
     * @param targetIndexPosition the target index position
     * @param withAnimation       true, scroll with animation
     */
    override fun setSelectedIndex(targetIndexPosition: Int, withAnimation: Boolean) {
        if (targetIndexPosition < 0 || targetIndexPosition >= itemCount)
            throw IndexOutOfBoundsException("Out of array bounds.")
        if (withAnimation) {
            executeAnimation(totalMoveY, 0 - itemHeight * targetIndexPosition)
        } else {
            totalMoveY = 0 - itemHeight * targetIndexPosition
            selectedPosition = targetIndexPosition
            offsetY = 0
            invalidate()
            onSelectedListener?.onSelected(context, selectedPosition)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var top = 0 - itemHeight
        for (i in 0 until drawCount) {
            defaultRectArray[i].set(0, top, 0, top + itemHeight)
            top += itemHeight
        }
        val heightSpec = MeasureSpec.makeMeasureSpec(itemHeight * showCount, MeasureSpec.EXACTLY)?:heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEmpty)
            return super.onTouchEvent(event)

        initVelocityTrackerIfNotExists()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //add event into velocity tracker.
                mVelocityTracker?.clear()
                //stop fling and reset fling direction
                flingDirection = 0
                mOverScroller?.forceFinished(true)
                animator?.let {
                    if (it.isRunning) {
                        isAnimatorCanceledForwardly = true
                        it.cancel()
                    }
                }
                lastX = event.x
                lastY = event.y
                //Make it as a click event when touch down,
                //and record touch down time stamp.
                touchDownTimeStamp = System.currentTimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
                //add event into velocity tracker and compute velocity.
                mVelocityTracker?.addMovement(event)
                val currentX = event.x
                val currentY = event.y
                val distance = (currentY - lastY).toInt()

                val direction: Int
                if (distance == 0)
                    return false

                //if moved, cancel click event
                touchDownTimeStamp = 0
                direction = distance / abs(distance)
                //initialize touch area
                rectF.set(0f, 0f, width.toFloat(), height.toFloat())
                if (rectF.contains(currentX, currentY)) {
                    //inside touch area, execute move event.
                    lastX = currentX
                    lastY = currentY
                    updateByTotalMoveY(totalMoveY + distance, direction)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (System.currentTimeMillis() - touchDownTimeStamp <= CLICK_EVENT_INTERNAL_TIME) {
                    //it's a click event, do it
                    executeClickEvent(event.x, event.y)
                    return false
                }

                //calculate current velocity
                val velocityTracker = mVelocityTracker
                velocityTracker?.computeCurrentVelocity(velocityUnits, mMaximumVelocity.toFloat())
                val currentVelocity = velocityTracker?.yVelocity ?: 0.0f
                recycleVelocityTracker()
                val tempFlingDirection = if (currentVelocity == 0f) 0 else if (currentVelocity < 0) -1 else 1
                if (abs(currentVelocity) >= mMinimumVelocity) {
                    //it's a fling event.
                    flingDirection = tempFlingDirection
                    mOverScroller?.fling(
                        0, totalMoveY, 0, currentVelocity.toInt(), 0, 0,
                        -(itemCount + showCount / 2) * itemHeight, showCount / 2 * itemHeight, 0, 0
                    )
                    invalidate()
                } else {
                    calculateSelectedIndex(totalMoveY, tempFlingDirection)
                    selectedPosition = calculateResult[0]
                    offsetY = calculateResult[1]
                    //execute rebound animation
                    executeAnimation(totalMoveY, 0 - selectedPosition * itemHeight)
                }
            }
        }
        return true
    }

    fun getShowCount(): Int {
        return showCount
    }
    fun getItemHeight(): Int {
        return itemHeight
    }
    fun getTotalMoveY(): Int {
        return totalMoveY
    }
    /**
     * Set the fling velocity units.
     * The default value is [.DEFAULT_VELOCITY_UNITS].
     * @param velocityUnits the velocity units
     */
    fun setVelocityUnits(velocityUnits: Int) {
        this.velocityUnits = abs(velocityUnits)
    }

    /**
     * Set the 3D rotation.
     *
     * @param wheelRotationX the rotate wheel base x axis
     */
    fun setWheelRotationX(wheelRotationX: Float) {
        if (this.wheelRotationX != wheelRotationX) {
            this.wheelRotationX = wheelRotationX
            invalidate()
        }
    }

    /**
     * Calculate average pixel length of show text.
     *
     * @return the average pixel length of show text
     */
    private fun calAverageShowTextLength(): Float {
        var totalLength = 0f
        var showText: String?
        for (wheel in items!!) {
            showText = wheel.showText
            if (showText.isNullOrEmpty())continue
            totalLength += textPaint.measureText(showText)
        }
        return totalLength / itemCount
    }

    /**
     * Execute click event.
     *
     * @return true, valid click event, else invalid.
     */
    private fun executeClickEvent(upX: Float, upY: Float) {
        var isValidTempSelectedIndex = false
        var tempSelectedIndex = selectedPosition - drawCount / 2
        for (i in 0 until drawCount) {
            rectF.set(drawRectArray[i])
            if (rectF.contains(upX, upY)) {
                isValidTempSelectedIndex = true
                break
            }
            tempSelectedIndex++
        }
        if (isValidTempSelectedIndex
            && tempSelectedIndex >= 0
            && tempSelectedIndex < itemCount
        ) {
            //Move to target selected index
            setSelectedIndex(tempSelectedIndex,true)
        }
    }

    private fun getItemAt(position: Int): IWheel? {
        return if (isEmpty || position < 0 || position >= itemCount) null else items?.get(position)
    }

    /**
     * Execute animation.
     */
    private fun executeAnimation(vararg values: Int) {
        //if it's invalid animation, call back immediately.
        if (invalidAnimation(*values)) {
            onSelectedListener?.onSelected(context, selectedPosition)
            return
        }
        var duration = 0
        for (i in values.indices) {
            if (i > 0) {
                duration += abs(n = values[i] - values[i - 1])
            }
        }
        if (duration == 0) {
            onSelectedListener?.onSelected(context, selectedPosition)
            return
        }
        createAnimatorIfNecessary()
        animator?.let {
            if(it.isRunning){
                isAnimatorCanceledForwardly = true
                it.cancel()
            }
            it.setIntValues(*values)
            it.duration = calSuitableDuration(duration).toLong()
            it.start()
        }
    }

    private fun invalidAnimation(vararg values: Int): Boolean {
        if (values.size < 2)  return true
        val firstValue = values[0]
        for (value in values) {
            if (firstValue != value)
                return false
        }
        return true
    }

    private fun calSuitableDuration(duration: Int): Int {
        var result = duration
        while (result > 1200) {
            result /= 2
        }
        return result
    }

    /**
     * Create auto-scroll animation.
     */
    private fun createAnimatorIfNecessary() {
        if (animator == null) {
            animator = ValueAnimator()
            animator?.addUpdateListener { animation ->
                val tempTotalMoveY = animation.animatedValue as Int
                updateByTotalMoveY(tempTotalMoveY, 0)
            }
            animator?.interpolator = LinearInterpolator()
            animator?.addListener(object : Animator.AnimatorListener {

                override fun onAnimationStart(animation: Animator) {
                    isAnimScrolling = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    isAnimScrolling = false
                    //Cancel animation forwardly.
                    if (isAnimatorCanceledForwardly) {
                        isAnimatorCanceledForwardly = false
                        return
                    }
                    onSelectedListener?.onSelected(context, selectedPosition)
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
        }
    }

    private fun updateByTotalMoveY(totalMoveY: Int, direction: Int) {
        calculateSelectedIndex(totalMoveY, direction)
        this.totalMoveY = totalMoveY
        this.selectedPosition = calculateResult[0]
        this.offsetY = calculateResult[1]
        invalidate()
    }

    private fun calculateSelectedIndex(totalMoveY: Int, direction: Int) {
        var selectedTempIndex = totalMoveY / (0 - itemHeight)
        var rest = totalMoveY % (0 - itemHeight)
        if (direction > 0 && rest != 0) {
            selectedTempIndex++
            rest = itemHeight - abs(rest)
        }
        //move up
        if (direction < 0 && abs(rest) >= itemHeight / 4) {
            selectedTempIndex++
        }
        //move down
        if (direction > 0 && abs(rest) >= itemHeight / 4) {
            selectedTempIndex--
        }

        selectedTempIndex = max(selectedTempIndex, 0)
        selectedTempIndex = min(selectedTempIndex, itemCount - 1)
        val offsetY = 0 - selectedTempIndex * itemHeight - totalMoveY
        calculateResult[0] = selectedTempIndex
        calculateResult[1] = offsetY
    }

    override fun computeScroll() {
        if (mOverScroller?.computeScrollOffset()!!) {
            totalMoveY = mOverScroller?.currY?:0
            updateByTotalMoveY(totalMoveY, 0)
            invalidate()
            return
        }

        if (flingDirection != 0) {
            val flingDirectionCopy = flingDirection
            flingDirection = 0
            calculateSelectedIndex(totalMoveY, flingDirectionCopy)
            selectedPosition = calculateResult[0]
            offsetY = calculateResult[1]
            //execute rebound animation
            executeAnimation( totalMoveY,0 - selectedPosition * itemHeight
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isEmpty)
            return
        var tempStartSelectedIndex = selectedPosition - drawCount / 2
        for (i in 0 until drawCount) {
            val rect = drawRectArray[i]
            rect.set(defaultRectArray[i])
            //record touch area for click event
            rect.left = 0
            rect.right = width
            if (tempStartSelectedIndex in 0 until itemCount) {
                drawItem(canvas, rect, getItemAt(tempStartSelectedIndex), -offsetY, textPaint)
            }
            tempStartSelectedIndex++
        }
        computeScroll()
    }

    private fun drawItem(canvas: Canvas, rect: Rect, item: IWheel?, offsetY: Int, textPaint: TextPaint) {
        val text = item?.showText ?: ""
        if (text.trim { it <= ' ' }.isEmpty()) return
        rect.offset(0, offsetY)
        textPaint.alpha = calAlpha(rect)
        val offsetX = if (totalOffsetX == 0) 0 else calOffsetX(totalOffsetX, rect)
        val w = textPaint.measureText(text)

       val startX = when {
           totalOffsetX > 0 -> {
               //show text align right
               val rightAlignPosition = (width + averageShowTextLength) / 2.0f
               rightAlignPosition - w + offsetX
           }
           totalOffsetX < 0 -> {
               //show text align left
               val leftAlignPosition = (width - averageShowTextLength) / 2.0f
               leftAlignPosition + offsetX
           }
           else -> //show text align center_horizontal
               (width - w) / 2.0f + offsetX
       }
        var centerX = width / 2.0f
        val centerY = rect.exactCenterY()
        val baseLine = centerY + textBaseLine
        newMatrix.reset()
        camera.save()
        camera.rotateX(calRotationX(rect, wheelRotationX))
        camera.getMatrix(newMatrix)
        camera.restore()
        newMatrix.preTranslate(-centerX, -centerY)
        newMatrix.postTranslate(centerX, centerY)
        if (totalOffsetX > 0) {
            val skewX = 0 - calSkewX(rect)
            centerX = (startX + w) / 2.0f
            newMatrix.setSkew(skewX, 0f, centerX, centerY)
        } else if (totalOffsetX < 0) {
            val skewX = calSkewX(rect)
            centerX = (startX + w) / 2.0f
            newMatrix.setSkew(skewX, 0f, centerX, centerY)
        }
        canvas.save()
        canvas.concat(newMatrix)
        canvas.drawText(text, startX, baseLine, textPaint)
        canvas.restore()
    }

    private fun calAlpha(rect: Rect): Int {
        val centerY = height / 2
        val distance = abs(centerY - rect.centerY())
        val totalDistance = itemHeight * (showCount / 2)
        val alpha = 0.6f * distance / totalDistance
        return ((1 - alpha) * 0xFF).toInt()
    }

    private fun calRotationX(rect: Rect, baseRotationX: Float): Float {
        val centerY = height / 2
        val distance = centerY - rect.centerY()
        val totalDistance = itemHeight * (showCount / 2)
        return baseRotationX * distance.toFloat() * 1.0f / totalDistance
    }

    private fun calSkewX(rect: Rect): Float {
        val centerY = height / 2
        val distance = centerY - rect.centerY()
        val totalDistance = itemHeight * (showCount / 2)
        return 0.3f * distance / totalDistance
    }

    private fun calOffsetX(totalOffsetX: Int, rect: Rect): Int {
        val centerY = height / 2
        val distance = abs(centerY - rect.centerY())
        val totalDistance = itemHeight * (showCount / 2)
        return totalOffsetX * distance / totalDistance
    }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        mVelocityTracker?.recycle()
        mVelocityTracker = null
    }

    interface OnSelectedListener {
        fun onSelected(context: Context, selectedIndex: Int)
    }
    companion object {
        private const val DEFAULT_ROTATION_X = 45.0f
        private const val DEFAULT_VELOCITY_UNITS = 600
        private const val CLICK_EVENT_INTERNAL_TIME: Long = 1000
    }
}

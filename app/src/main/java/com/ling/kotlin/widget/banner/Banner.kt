//package com.ling.kotlin.widget.banner
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.res.Resources
//import android.graphics.Color
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.GradientDrawable
//import android.graphics.drawable.LayerDrawable
//import android.os.Handler
//import android.os.Handler.Callback
//import android.os.Message
//import android.os.Parcel
//import android.os.Parcelable
//import android.util.AttributeSet
//import android.util.SparseArray
//import android.util.TypedValue
//import android.view.Gravity
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.DecelerateInterpolator
//import android.view.animation.Interpolator
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.Scroller
//import androidx.core.view.GravityCompat
//import androidx.core.view.ViewCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.OrientationHelper
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.*
//import com.ling.kotlin.R
//import java.util.ArrayList
//import kotlin.math.abs
//import kotlin.math.round
//
//
//class BannerLayoutManager @JvmOverloads constructor(
//    context: Context,
//    orientation: Int = HORIZONTAL,
//    reverseLayout: Boolean = false
//) : RecyclerView.LayoutManager() {
//
//    private val positionCache = SparseArray<View>()
//    /**
//     * use for handle focus
//     */
//    private var currentFocusView: View? = null
//    /**
//     * 返回LayoutManager在分离时是否会回收子项RecyclerView。
//     */
//    var recycleChildrenOnDetach: Boolean = false
//    private var smoothScrollInterpolator: Interpolator? = null
//    var infinite = true
//    /**
//     * the mInterval of each item's mOffset
//     */
//    var interval: Float = 0.toFloat()
//
//    protected var decoratedMeasurement: Int = 0
//
//    protected var decoratedMeasurementInOther: Int = 0
//
//    protected var spaceMain: Int = 0
//
//    protected var spaceInOther: Int = 0
//
//    private var leftItems: Int = 0
//
//    private var rightItems: Int = 0
//    /**
//     * @return the mInterval of each item's mOffset
//     */
//    /**
//     * @return the mInterval of each item's mOffset
//     */
//    private var itemSpace = 20
//
//    private var centerScale = 1.2f
//    private var moveSpeed = 1.0f
//    /**
//     * When LayoutManager needs to scroll to a position, it sets this variable and requests a
//     * layout which will check this variable and re-layout accordingly.
//     */
//    private var pendingScrollPosition = NO_POSITION
//
//    /**
//     * The offset of property which will change while scrolling
//     */
//    var offset: Float = 0.toFloat()
//    /**
//     * This keeps the final value for how LayoutManager should start laying out views.
//     * It is calculated by checking [.getReverseLayout] and View's layout direction.
//     * [.onLayoutChildren] is run.
//     */
//    private var shouldReverseLayout = false
//    private var pendingSavedState: SavedState? = null
//
//    protected var mOrientationHelper: OrientationHelper? = null
//    private var mDistanceToBottom = INVALID_SIZE
//
//    /**
//     * 工作方式和{@link android.widget.AbsListView#setSmoothScrollbarEnabled(boolean)}.
//     * see {@link android.widget.AbsListView#setSmoothScrollbarEnabled(boolean)}
//     * 启用平滑滚动条时，将计算滚动条滑块的位置和大小
//     * 基于可见项目中可见像素的数量。 然而，这假定所有
//     * 列表项具有相似或相等的宽度或高度（取决于列表方向）。
//     * 如果使用项目具有不同尺寸的列表，滚动条将会更改
//     * 当用户滚动列表时的外观。 要避免此问题，您需要禁用
//     *  这个性质。
//     * <p>
//     * 禁用平滑滚动条时，滚动条滑块的位置和大小基于
//     * 仅取决于适配器中的项目数和内部可见项的位置
//     * 适配器。 当用户浏览项目列表时，这提供了稳定的滚动条
//     * 具有不同的宽度/高度。
//     * @param enabled是否启用平滑滚动条。
//     *  @see #setSmoothScrollbarEnabled（boolean）
//     * 返回平滑滚动条功能的当前状态。 它默认启用。
//     * @return 如果启用了平滑滚动条，则为True，否则为false。
//     * @see #setSmoothScrollbarEnabled（boolean）
//     */
//    var smoothScrollbarEnabled = true
//    var enableBringCenterToFront: Boolean = false
//        set(bringCenterToTop) {
//            assertNotInLayoutOrScroll(null)
//            if (field != bringCenterToTop) {
//                field = bringCenterToTop
//                requestLayout()
//            }
//        }
//
//    /**
//     * 设置最大可见项目数，{@ link #DADERMINE_BY_MAX_AND_MIN}表示现在尚未设置
//     * 它将使用{@link #maxRemoveOffset（）}和{@link #minRemoveOffset（）}来处理范围
//     * @param maxVisibleItemCount Max visible item count
//     */
//    private var maxVisibleItemCount: Int = DETERMINE_BY_MAX_AND_MIN
//
//        set(mMaxVisibleItemCount) {
//            assertNotInLayoutOrScroll(null)
//            if (field != mMaxVisibleItemCount) {
//                field = mMaxVisibleItemCount
//                removeAllViews()
//            }
//        }
//
//    var orientation: Int = 0
//        set(value) {
//            if (value != HORIZONTAL && value != VERTICAL) {
//                throw IllegalArgumentException("invalid orientation:$orientation")
//            }
//            assertNotInLayoutOrScroll(null)
//            if (field != value) {
//                field = value
//                mOrientationHelper = null
//                mDistanceToBottom = INVALID_SIZE
//                removeAllViews()
//            }
//        }
//
//    /**
//     * 用于反转项目遍历和布局顺序。
//     * 此行为类似于RTL视图的布局更改。 设置为true时，第一项是
//     * 在UI的末尾布局，第二个项目在它之前布局等。
//     *  <p>
//     * 对于水平布局，它取决于布局方向。
//     * 设置为true时，如果{@link RecyclerView}是LTR，那么它将是
//     * 来自RTL的布局，如果{@link RecyclerView}}是RTL，它将进行布局
//     * 来自LTR。
//     */
//    internal var reverseLayout = false
//        set(value) {
//            assertNotInLayoutOrScroll(null)
//            if (field != value) {
//                field = value
//                removeAllViews()
//            }
//        }
//
//    var onPageChangeListener:OnPageChangeListener?=null
//    set(value) {
//        field = value
//    }
//
//    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
//        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//    }
//
//    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
//        super.onDetachedFromWindow(view, recycler)
//        recycler?.let {
//            if(recycleChildrenOnDetach){
//            removeAndRecycleAllViews(it)
//            it.clear()
//        } }
//    }
//
//    override fun onSaveInstanceState(): Parcelable? {
//        pendingSavedState?.let {
//            val(position,offset,isReverserLayout) = it
//            return SavedState(position, offset, isReverserLayout)
//        }
//        return SavedState(pendingScrollPosition,offset ,shouldReverseLayout)
//    }
//
//    override fun onRestoreInstanceState(state: Parcelable?) {
//        if(state is SavedState){
//            val(position,offset,isReverserLayout) = state
//            pendingSavedState = SavedState(position,offset,isReverserLayout)
//            requestLayout()
//        }
//    }
//
//    override fun canScrollHorizontally(): Boolean {
//        return orientation == HORIZONTAL
//    }
//
//    override fun canScrollVertically(): Boolean {
//        return orientation == VERTICAL
//    }
//
//    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
//        val offsetPosition = getOffsetToPosition(position)
//        if(orientation == VERTICAL) recyclerView?.smoothScrollBy(0,offsetPosition,smoothScrollInterpolator) else recyclerView?.smoothScrollBy(offsetPosition,0,smoothScrollInterpolator)
//    }
//
//    override fun scrollToPosition(position: Int) {
//        if(!infinite && (position < 0 || position >= itemCount))return
//        pendingScrollPosition = position
//        offset = if(shouldReverseLayout) position * -interval else position * interval
//    }
//
//    override fun onLayoutCompleted(state: RecyclerView.State?) {
//        super.onLayoutCompleted(state)
//        pendingSavedState = null
//        pendingScrollPosition = NO_POSITION
//    }
//
//    override fun onAddFocusables(
//        recyclerView: RecyclerView,
//        views: ArrayList<View>,
//        direction: Int,
//        focusableMode: Int
//    ): Boolean {
//        val currentPosition = getCurrentPosition()
//        val currentView = findViewByPosition(currentPosition) ?: return true
//        if (recyclerView.hasFocus()) {
//            val movement = getMovement(direction)
//            if (movement != DIRECTION_NO_WHERE) {
//                val targetPosition = if (movement == DIRECTION_BACKWARD)
//                    currentPosition - 1
//                else
//                    currentPosition + 1
//                recyclerView.smoothScrollToPosition(targetPosition)
//            }
//        } else {
//            currentView.addFocusables(views, direction, focusableMode)
//        }
//        return true
//    }
//
//
//    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
//        removeAllViews()
//        offset = 0f
//    }
//
//
//    override fun computeHorizontalScrollOffset(state: RecyclerView.State): Int {
//        return computeScrollOffset()
//    }
//
//    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
//        return computeScrollOffset()
//    }
//
//    override fun computeHorizontalScrollExtent(state: RecyclerView.State): Int {
//        return computeScrollExtent()
//    }
//
//    override fun computeVerticalScrollExtent(state: RecyclerView.State): Int {
//        return computeScrollExtent()
//    }
//
//    override fun computeHorizontalScrollRange(state: RecyclerView.State): Int {
//        return computeScrollRange()
//    }
//
//    override fun computeVerticalScrollRange(state: RecyclerView.State): Int {
//        return computeScrollRange()
//    }
//
//    private fun computeScrollOffset(): Int {
//        if (childCount == 0) {
//            return 0
//        }
//
//        if (!smoothScrollbarEnabled) {
//            return if (!shouldReverseLayout)
//                getCurrentPosition()
//            else
//                itemCount - getCurrentPosition() - 1
//        }
//
//        val realOffset = getOffsetOfRightAdapterPosition()
//        return if (!shouldReverseLayout) realOffset.toInt() else ((itemCount - 1) * interval + realOffset).toInt()
//    }
//
//    private fun computeScrollExtent(): Int {
//        if (childCount == 0) {
//            return 0
//        }
//
//        return if (!smoothScrollbarEnabled) {
//            1
//        } else interval.toInt()
//
//    }
//
//    private fun computeScrollRange(): Int {
//        if (childCount == 0) {
//            return 0
//        }
//
//        return if (!smoothScrollbarEnabled) {
//            itemCount
//        } else (itemCount * interval).toInt()
//
//    }
//
//    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
//        return if (orientation == VERTICAL) {
//            0
//        } else scrollBy(dx, recycler, state)
//    }
//
//    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
//        return if (orientation == HORIZONTAL) {
//            0
//        } else scrollBy(dy, recycler, state)
//    }
//
//    private fun scrollBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
//        if (childCount == 0 || dy == 0) {
//            return 0
//        }
//        ensureLayoutState()
//        var willScroll = dy
//
//        var realDx = dy / getDistanceRatio()
//        if (abs(realDx) < 0.00000001f) {
//            return 0
//        }
//        val targetOffset = offset + realDx
//
//        //handle the boundary
//        if (!infinite && targetOffset < getMinOffset()) {
//            willScroll -= ((targetOffset - getMinOffset()) * getDistanceRatio()).toInt()
//        } else if (!infinite && targetOffset > getMaxOffset()) {
//            willScroll = ((getMaxOffset() - offset) * getDistanceRatio()).toInt()
//        }
//
//        realDx = willScroll / getDistanceRatio()
//
//        offset += realDx
//
//        //handle recycle
//        recycler?.let {  layoutItems(it) }
//        return willScroll
//    }
//
//
//    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
//        if(state?.itemCount == 0) {
//            recycler?.let { removeAndRecycleAllViews(it) }
//            offset = 0f
//            return
//        }
//        ensureLayoutState()
//        resolveShouldLayoutReverse()
//        val scrap = recycler?.getViewForPosition(0)
//        scrap?.let {
//            measureChildWithMargins(it, 0, 0)
//            decoratedMeasurement = mOrientationHelper?.getDecoratedMeasurement(it)!!
//            decoratedMeasurementInOther = mOrientationHelper?.getDecoratedMeasurementInOther(it)!!
//        }
//        moveSpeed = ((mOrientationHelper?.totalSpace?.minus(decoratedMeasurement))?.div(2))?.toFloat()!!
//        spaceInOther = if(mDistanceToBottom == INVALID_SIZE) (getTotalSpaceInOther() - decoratedMeasurementInOther) /2  else getTotalSpaceInOther() - decoratedMeasurementInOther - mDistanceToBottom
//        interval = setInterval()
//        setUp()
//        leftItems = (abs(minRemoveOffset() / interval) + 1).toInt()
//        rightItems = (abs(maxRemoveOffset() / interval) + 1).toInt()
//
//        pendingSavedState?.let {
//            val(position,offset,isReverseLayout) = it
//            shouldReverseLayout = isReverseLayout
//            pendingScrollPosition = position
//            this.offset = offset
//        }
//        if(pendingScrollPosition != NO_POSITION){
//            offset = if (shouldReverseLayout) pendingScrollPosition * -interval else pendingScrollPosition * interval
//        }
//        if (recycler != null) {
//            detachAndScrapAttachedViews(recycler)
//            layoutItems(recycler)
//        }
//    }
//
//    private fun layoutItems(recycler: RecyclerView.Recycler){
//        detachAndScrapAttachedViews(recycler)
//        positionCache.clear()
//        val itemCount = itemCount
//        if(itemCount == 0)return
//        val currentPos = if(shouldReverseLayout) -getCurrentPositionOffset() else getCurrentPositionOffset()
//        var start = currentPos - leftItems
//        var end = currentPos +  rightItems
//        if(useMaxVisibleCount()){
//            var isEven = maxVisibleItemCount % 2 == 0
//            var mOffset: Int
//            if(isEven){
//                mOffset = maxVisibleItemCount / 2
//                start = currentPos - mOffset + 1
//                end = currentPos + mOffset+ 1
//            }else{
//                mOffset = (maxVisibleItemCount -1) / 2
//                start = currentPos - mOffset
//                end = currentPos + mOffset + 1
//            }
//        }
//        if(!infinite){
//            if(start < 0){
//                start = 0
//                if(useMaxVisibleCount()) end = maxVisibleItemCount
//            }
//            if(end > itemCount) end = itemCount
//        }
//       var lastOrderWeight = Float.MIN_VALUE
//        for (i in start until end) {
//            if (useMaxVisibleCount() || !removeCondition(getProperty(i) - offset)) {
//                // start and end base on current position,
//                // so we need to calculate the adapter position
//                var adapterPosition = i
//                if (i >= itemCount) {
//                    adapterPosition %= itemCount
//                } else if (i < 0) {
//                    var delta = -adapterPosition % itemCount
//                    if (delta == 0) delta = itemCount
//                    adapterPosition = itemCount - delta
//                }
//                val scrap = recycler.getViewForPosition(adapterPosition)
//                measureChildWithMargins(scrap, 0, 0)
//                resetViewProperty(scrap)
//                // we need i to calculate the real offset of current view
//                val targetOffset = getProperty(i) - offset
//                layoutScrap(scrap, targetOffset)
//                val orderWeight = if (enableBringCenterToFront) setViewElevation(scrap, targetOffset) else adapterPosition.toFloat()
//                if (orderWeight > lastOrderWeight) {
//                    addView(scrap)
//                } else {
//                    addView(scrap, 0)
//                }
//                if (i == currentPos) currentFocusView = scrap
//                lastOrderWeight = orderWeight
//                positionCache.put(i, scrap)
//            }
//        }
//        currentFocusView?.requestFocus()
//    }
//
//    fun getCurrentPosition():Int{
//        if(itemCount == 0) return 0
//        var position = getCurrentPositionOffset()
//        if(!infinite) return abs(position)
//        position = if(!shouldReverseLayout)( if(position >= 0) position % itemCount else itemCount + position % itemCount) else (if(position > 0) itemCount - position % itemCount else -position % itemCount)
//        return  if(position == itemCount) 0 else position
//    }
//
//    fun getOffsetToPosition(position: Int):Int{
//        var offsetPosition :Float = if(infinite){
//            getCurrentPositionOffset() + (if(!shouldReverseLayout) position - getCurrentPosition() else getCurrentPosition() - position) * (interval - offset) * getDistanceRatio()
//        }else{
//            (position * if (!shouldReverseLayout) interval else -interval - offset) * getDistanceRatio()
//        }
//        return offsetPosition.toInt()
//    }
//
//    /**
//     *
//     * @return the dy between center and current position
//     */
//    fun getOffsetToCenter(): Int {
//        return if (infinite) ((getCurrentPositionOffset() * interval - offset) * getDistanceRatio()).toInt() else ((getCurrentPosition() * if (!shouldReverseLayout) interval else -interval - offset) * getDistanceRatio()).toInt()
//    }
//
//    /**
//     * Sometimes we need to get the right offset of matching adapter position
//     * cause when [.mInfinite] is set true, there will be no limitation of [.mOffset]
//     */
//    private fun getOffsetOfRightAdapterPosition(): Float {
//        return if (shouldReverseLayout)
//            if (infinite) if (offset <= 0) offset % (interval * itemCount) else itemCount * -interval + offset % (interval * itemCount) else offset
//        else
//            if (infinite) if (offset >= 0) offset % (interval * itemCount) else itemCount * interval + offset % (interval * itemCount) else offset
//    }
//
//    private fun getCurrentPositionOffset(): Int = round(offset / interval).toInt()
//
//    fun getDistanceRatio():Float = if(moveSpeed == 0f) Float.MAX_VALUE else 1 / moveSpeed
//
//    /**
//     * Calculates the view layout order. (e.g. from end to start or start to end)
//     * RTL layout support is applied automatically. So if layout is RTL and
//     * {@link #getReverseLayout()} is {@code true}, elements will be laid out starting from left.
//     */
//    private fun resolveShouldLayoutReverse(){
//        if(orientation == HORIZONTAL && layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL){
//            reverseLayout != reverseLayout
//        }
//    }
//    private fun ensureLayoutState() {
//        if (mOrientationHelper == null) {
//            mOrientationHelper = OrientationHelper.createOrientationHelper(this, orientation)
//        }
//    }
//    fun getTotalSpaceInOther(): Int {
//        return if (orientation == HORIZONTAL) height - paddingTop- paddingBottom else width - paddingLeft - paddingRight
//    }
//
//    protected fun setInterval(): Float {
//        return decoratedMeasurement * ((centerScale - 1) / 2 + 1) + itemSpace
//    }
//
//    fun setItemSpace(itemSpace: Int) {
//        this.itemSpace = itemSpace
//    }
//
//    fun setCenterScale(centerScale: Float) {
//        this.centerScale = centerScale
//    }
//    /**
//     * You can set up your own properties here or change the exist properties like mSpaceMain and mSpaceInOther
//     */
//    protected fun setUp() {
//
//    }
//
//
//    /**
//     * when the target offset reach this,
//     * the view will be removed and recycled in [.layoutItems]
//     */
//    protected fun maxRemoveOffset(): Float{
//        mOrientationHelper?.let {
//            return (it.totalSpace - spaceMain).toFloat()
//        }
//        return 0f
//    }
//
//    /**
//     * when the target offset reach this,
//     * the view will be removed and recycled in [.layoutItems]
//     */
//    protected fun minRemoveOffset(): Float {
//        return (-decoratedMeasurement - mOrientationHelper?.startAfterPadding!! - spaceMain).toFloat()
//    }
//
//    private fun useMaxVisibleCount(): Boolean {
//        return maxVisibleItemCount != DETERMINE_BY_MAX_AND_MIN
//    }
//
//    private fun getProperty(position: Int): Float {
//        return if (shouldReverseLayout) position * -interval else position * interval
//    }
//
//    private fun removeCondition(targetOffset: Float): Boolean {
//        return targetOffset > maxRemoveOffset() || targetOffset < minRemoveOffset()
//    }
//
//    private fun resetViewProperty(v: View) {
//        v.rotation = 0f
//        v.rotationY = 0f
//        v.rotationX = 0f
//        v.scaleX = 1f
//        v.scaleY = 1f
//        v.alpha = 1f
//    }
//
//
//    private fun layoutScrap(scrap: View, targetOffset: Float) {
//
//        val left = calItemLeft(scrap, targetOffset)
//        val top = calItemTop(scrap, targetOffset)
//        if (orientation == VERTICAL) {
//            layoutDecorated(
//                scrap, spaceInOther + left, spaceMain + top,
//                spaceInOther + left + decoratedMeasurementInOther, spaceMain + top + decoratedMeasurement
//            )
//        } else {
//            layoutDecorated(
//                scrap, spaceMain + left, spaceInOther + top,
//                spaceMain + left + decoratedMeasurement, spaceInOther + top + decoratedMeasurementInOther
//            )
//        }
//        setItemViewProperty(scrap, targetOffset)
//    }
//
//    protected fun calItemLeft(itemView: View, targetOffset: Float): Int {
//        return if (orientation == VERTICAL) 0 else targetOffset.toInt()
//    }
//
//    protected fun calItemTop(itemView: View, targetOffset: Float): Int {
//        return if (orientation == VERTICAL) targetOffset.toInt() else 0
//    }
//
//    fun setMoveSpeed(moveSpeed: Float) {
//        assertNotInLayoutOrScroll(null)
//        if (this.moveSpeed == moveSpeed) return
//        this.moveSpeed = moveSpeed
//    }
//
//    protected fun setItemViewProperty(itemView: View, targetOffset: Float) {
//        val scale = calculateScale(targetOffset + spaceMain)
//        itemView.scaleX = scale
//        itemView.scaleY = scale
//    }
//
//    /**
//     * @param x start positon of the view you want scale
//     * @return the scale rate of current scroll mOffset
//     */
//    private fun calculateScale(x: Float): Float {
//        val deltaX = abs(x - ((mOrientationHelper?.totalSpace?.minus(decoratedMeasurement))?.div(2f) ?: 0f))
//        var diff = 0f
//        if (decoratedMeasurement - deltaX > 0) diff = decoratedMeasurement - deltaX
//        return (centerScale - 1f) / decoratedMeasurement * diff + 1
//    }
//
//    /**
//     * cause elevation is not support below api 21,
//     * so you can set your elevation here for supporting it below api 21
//     * or you can just setElevation in [.setItemViewProperty]
//     */
//    protected fun setViewElevation(itemView: View, targetOffset: Float): Float {
//        return 0f
//    }
//    fun getMaxOffset(): Float {
//        return (if (!shouldReverseLayout) (itemCount - 1) * interval else 0) as Float
//    }
//
//    fun getMinOffset(): Float {
//        return (if (!shouldReverseLayout) 0 else -(itemCount - 1) * interval) as Float
//    }
//
//    companion object {
//        const val DETERMINE_BY_MAX_AND_MIN = -1
//        const val HORIZONTAL = OrientationHelper.HORIZONTAL
//        const val VERTICAL = OrientationHelper.VERTICAL
//        private const val DIRECTION_NO_WHERE = -1
//        private const val DIRECTION_FORWARD = 0
//        private const val DIRECTION_BACKWARD = 1
//        private const val INVALID_SIZE = Integer.MAX_VALUE
//
//    }
//
//    private fun getMovement(direction: Int): Int {
//        return if (orientation == VERTICAL) {
//            if (direction == View.FOCUS_UP) {
//                if (shouldReverseLayout) DIRECTION_FORWARD else DIRECTION_BACKWARD
//            } else if (direction == View.FOCUS_DOWN) {
//                if (shouldReverseLayout) DIRECTION_BACKWARD else DIRECTION_FORWARD
//            } else {
//                DIRECTION_NO_WHERE
//            }
//        } else {
//            if (direction == View.FOCUS_LEFT) {
//                if (shouldReverseLayout) DIRECTION_FORWARD else DIRECTION_BACKWARD
//            } else if (direction == View.FOCUS_RIGHT) {
//                if (shouldReverseLayout) DIRECTION_BACKWARD else DIRECTION_FORWARD
//            } else {
//                DIRECTION_NO_WHERE
//            }
//        }
//    }
//    init {
//        enableBringCenterToFront = true
//        maxVisibleItemCount = 3
//        this.orientation = orientation
//        this.reverseLayout = reverseLayout
//        isAutoMeasureEnabled = true
//        isItemPrefetchEnabled = false
//    }
//
//    internal data class SavedState(var position: Int, var offset: Float, var isReverserLayout: Boolean) : Parcelable {
//        constructor(source: Parcel) : this(
//            source.readInt(),
//            source.readFloat(),
//            1 == source.readInt()
//        )
//        override fun describeContents() = 0
//
//        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
//            writeInt(position)
//            writeFloat(offset)
//            writeInt((if (isReverserLayout) 1 else 0))
//        }
//
//        companion object {
//            @JvmField
//            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
//                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
//                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
//            }
//        }
//    }
//}
//
///**
// * Class intended to support snapping for a [RecyclerView]
// * which use [BannerLayoutManager] as its [androidx.recyclerview.widget.RecyclerView.LayoutManager].
// *
// *
// * The implementation will snap the center of the target child view to the center of
// * the attached [RecyclerView].
// */
//class CenterSnapHelper : RecyclerView.OnFlingListener() {
//
//    private var mRecyclerView: RecyclerView? = null
//    private lateinit var mGravityScroller: Scroller
//
//    /**
//     * when the dataSet is extremely large
//     * [.snapToCenterView]
//     * may keep calling itself because the accuracy of float
//     */
//    private var snapToCenter = false
//
//    // Handles the snap on scroll case.
//    private val mScrollListener = object : RecyclerView.OnScrollListener() {
//
//         private var mScrolled = false
//
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//
//            val layoutManager = recyclerView.layoutManager as BannerLayoutManager?
//            val onPageChangeListener = layoutManager?.onPageChangeListener
//            onPageChangeListener?.onPageScrollStateChanged(newState)
//
//            if (newState == SCROLL_STATE_IDLE && mScrolled) {
//                mScrolled = false
//                if (!snapToCenter) {
//                    snapToCenter = true
//                    snapToCenterView(layoutManager, onPageChangeListener)
//                } else {
//                    snapToCenter = false
//                }
//            }
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            if (dx != 0 || dy != 0) {
//                mScrolled = true
//            }
//        }
//    }
//
//    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
//        val layoutManager = mRecyclerView!!.layoutManager as BannerLayoutManager? ?: return false
//        mRecyclerView?.adapter ?: return false
//        if (!layoutManager.infinite && (layoutManager.offset == layoutManager.getMaxOffset() || layoutManager.offset == layoutManager.getMinOffset())) return false
//
//        mRecyclerView?.let {
//            val minFlingVelocity = it.minFlingVelocity
//            mGravityScroller.fling(0, 0, velocityX, velocityY,  Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE )
//
//            if (layoutManager.orientation == BannerLayoutManager.VERTICAL && abs(velocityY) > minFlingVelocity) {
//                val currentPosition = layoutManager.getCurrentPosition()
//                val offsetPosition = (mGravityScroller.finalY.toFloat() / layoutManager.interval / layoutManager.getDistanceRatio()).toInt()
//                it.smoothScrollToPosition(if (layoutManager.reverseLayout) currentPosition - offsetPosition else currentPosition + offsetPosition
//                )
//                return true
//            } else if (layoutManager.orientation == BannerLayoutManager.HORIZONTAL && abs(velocityX) > minFlingVelocity) {
//                val currentPosition = layoutManager.getCurrentPosition()
//                val offsetPosition = (mGravityScroller.finalX.toFloat() / layoutManager.interval / layoutManager.getDistanceRatio()).toInt()
//                it.smoothScrollToPosition( if (layoutManager.reverseLayout) currentPosition - offsetPosition else currentPosition + offsetPosition)
//                return true
//            }
//        }
//
//        return true
//    }
//
//    /**
//     * Please attach after {[androidx.recyclerview.widget.RecyclerView.LayoutManager] is setting}
//     * Attaches the [CenterSnapHelper] to the provided RecyclerView, by calling
//     * [RecyclerView.setOnFlingListener].
//     * You can call this method with `null` to detach it from the current RecyclerView.
//     *
//     * @param recyclerView The RecyclerView instance to which you want to add this helper or
//     * `null` if you want to remove CenterSnapHelper from the current
//     * RecyclerView.
//     * @throws IllegalArgumentException if there is already a [RecyclerView.OnFlingListener]
//     * attached to the provided [RecyclerView].
//     */
//    @Throws(IllegalStateException::class)
//    fun attachToRecyclerView(recyclerView: RecyclerView?) {
//        if (mRecyclerView === recyclerView) {
//            return  // nothing to do
//        }
//        if (mRecyclerView != null) {
//            destroyCallbacks()
//        }
//        mRecyclerView = recyclerView
//        mRecyclerView?.let {
//            val layoutManager = it.layoutManager as? BannerLayoutManager ?: return
//            setupCallbacks()
//            mGravityScroller = Scroller(it.context, DecelerateInterpolator() )
//            snapToCenterView(layoutManager,layoutManager.onPageChangeListener )
//        }
//
//    }
//
//    internal fun snapToCenterView( layoutManager: BannerLayoutManager?,listener: OnPageChangeListener?) {
//        layoutManager?.let {
//            val delta = it.getOffsetToCenter()
//            if (delta != 0) {
//                if (it.orientation == BannerLayoutManager.VERTICAL)
//                    mRecyclerView?.smoothScrollBy(0, delta)
//                else
//                    mRecyclerView?.smoothScrollBy(delta, 0)
//            } else {
//                // set it false to make smoothScrollToPosition keep trigger the listener
//                snapToCenter = false
//            }
//            listener?.onPageSelected(it.getCurrentPosition())
//        }
//    }
//
//    /**
//     * Called when an instance of a [RecyclerView] is attached.
//     */
//    @Throws(IllegalStateException::class)
//    internal fun setupCallbacks() {
//        if (mRecyclerView?.onFlingListener != null) {
//            throw IllegalStateException("An instance of OnFlingListener already set.")
//        }
//        mRecyclerView?.addOnScrollListener(mScrollListener)
//        mRecyclerView?.onFlingListener = this
//    }
//
//    /**
//     * Called when the instance of a [RecyclerView] is detached.
//     */
//    internal fun destroyCallbacks() {
//        mRecyclerView?.removeOnScrollListener(mScrollListener)
//        mRecyclerView?.onFlingListener = null
//    }
//}
//
//
//class BannerLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
//    FrameLayout(context, attrs, defStyleAttr) {
//
//    private var autoPlayDuration: Int = 0//刷新间隔时间
//
//    private var showIndicator: Boolean = false//是否显示指示器
//
//    private var mSelectedDrawable: Drawable? = null
//    private var mUnselectedDrawable: Drawable? = null
//    private var indicatorAdapter: IndicatorAdapter? = null
//    private var indicatorMargin: Int = 0//指示器间距
//
//    private lateinit  var indicatorContainer: RecyclerView
//    private lateinit var mRecyclerView: RecyclerView
//    private lateinit var mLayoutManager: BannerLayoutManager
//
//    private val WHAT_AUTO_PLAY = 1000
//
//    private var hasInit: Boolean = false
//    private var bannerSize = 1
//    private var currentIndex: Int = 0
//    /**
//     * 设置是否自动播放（上锁）
//     *
//     * @param playing 开始播放
//     */
//    var isPlaying = false
//        @Synchronized private set(playing) {
//            if (isAutoPlaying && hasInit) {
//                if (!this.isPlaying && playing) {
//                    mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration.toLong())
//                    field = true
//                } else if (this.isPlaying && !playing) {
//                    mHandler.removeMessages(WHAT_AUTO_PLAY)
//                    field = false
//                }
//            }
//        }
//
//    private var isAutoPlaying = true
//    private var itemSpace: Int = 0
//    private var centerScale: Float = 0.toFloat()
//    private var moveSpeed: Float = 0.toFloat()
//    private val mHandler = Handler(Callback { msg ->
//        if (msg.what == WHAT_AUTO_PLAY) {
//            if (currentIndex == mLayoutManager.getCurrentPosition()) {
//                ++currentIndex
//                mRecyclerView.smoothScrollToPosition(currentIndex)
//                send()
//                refreshIndicator()
//            }
//        }
//        false
//    })
//
//    private fun send(){
//        mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration.toLong())
//    }
//    init {
//        initView(context, attrs)
//    }
//
//    @SuppressLint("WrongConstant")
//    private fun initView(context: Context, attrs: AttributeSet?) {
//
//        val a = context.obtainStyledAttributes(attrs, R.styleable.BannerLayout)
//        showIndicator = a.getBoolean(R.styleable.BannerLayout_showIndicator, true)
//        autoPlayDuration = a.getInt(R.styleable.BannerLayout_interval, 3000)
//        isAutoPlaying = a.getBoolean(R.styleable.BannerLayout_autoPlaying, true)
//        itemSpace = a.getInt(R.styleable.BannerLayout_itemSpace, 20)
//        centerScale = a.getFloat(R.styleable.BannerLayout_centerScale, 1.0f)
//        moveSpeed = a.getFloat(R.styleable.BannerLayout_moveSpeed, 1.0f)
//        if (mSelectedDrawable == null) {
//            //绘制默认选中状态图形
//            val selectedGradientDrawable = GradientDrawable()
//            selectedGradientDrawable.shape = GradientDrawable.OVAL
//            selectedGradientDrawable.setColor(Color.RED)
//            selectedGradientDrawable.setSize(dp2px(5), dp2px(5))
//            selectedGradientDrawable.cornerRadius = (dp2px(5) / 2).toFloat()
//            mSelectedDrawable = LayerDrawable(arrayOf<Drawable>(selectedGradientDrawable))
//        }
//        if (mUnselectedDrawable == null) {
//            //绘制默认未选中状态图形
//            val unSelectedGradientDrawable = GradientDrawable()
//            unSelectedGradientDrawable.shape = GradientDrawable.OVAL
//            unSelectedGradientDrawable.setColor(Color.GRAY)
//            unSelectedGradientDrawable.setSize(dp2px(5), dp2px(5))
//            unSelectedGradientDrawable.cornerRadius = (dp2px(5) / 2).toFloat()
//            mUnselectedDrawable = LayerDrawable(arrayOf<Drawable>(unSelectedGradientDrawable))
//        }
//
//        indicatorMargin = dp2px(4)
//        val marginLeft = dp2px(16)
//        val marginRight = dp2px(0)
//        val marginBottom = dp2px(11)
//        val gravity = GravityCompat.START
//        val o = a.getInt(R.styleable.BannerLayout_orientation, 0)
//        val orientation = if (o == 1) OrientationHelper.VERTICAL else OrientationHelper.HORIZONTAL
//        a.recycle()
//        //轮播图部分
//        mRecyclerView = RecyclerView(context)
//        val vpLayoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
//        addView(mRecyclerView, vpLayoutParams)
//        mLayoutManager = BannerLayoutManager(getContext(), orientation)
//        mLayoutManager.setItemSpace(itemSpace)
//        mLayoutManager.setCenterScale(centerScale)
//        mLayoutManager.setMoveSpeed(moveSpeed)
//        mRecyclerView.layoutManager = mLayoutManager
//        CenterSnapHelper().attachToRecyclerView(mRecyclerView)
//
//        //指示器部分
//        indicatorContainer = RecyclerView(context)
//        val indicatorLayoutManager = LinearLayoutManager(context, orientation , false)
//        indicatorContainer.layoutManager = indicatorLayoutManager
//        indicatorAdapter = IndicatorAdapter()
//        indicatorContainer.adapter = indicatorAdapter
//        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//        params.gravity = Gravity.BOTTOM or gravity
//        params.setMargins(marginLeft, 0, marginRight, marginBottom)
//        addView(indicatorContainer, params)
//        if (!showIndicator) {
//            indicatorContainer!!.visibility = View.GONE
//        }
//    }
//
//    // 设置是否禁止滚动播放
//    fun setAutoPlaying(isAutoPlaying: Boolean) {
//        this.isAutoPlaying = isAutoPlaying
//        isPlaying = this.isAutoPlaying
//    }
//
//    //设置是否显示指示器
//    fun setShowIndicator(showIndicator: Boolean) {
//        this.showIndicator = showIndicator
//        indicatorContainer.visibility = if (showIndicator) View.VISIBLE else View.GONE
//    }
//
//    //设置当前图片缩放系数
//    fun setCenterScale(centerScale: Float) {
//        this.centerScale = centerScale
//        mLayoutManager.setCenterScale(centerScale)
//    }
//
//    //设置跟随手指的移动速度
//    fun setMoveSpeed(moveSpeed: Float) {
//        this.moveSpeed = moveSpeed
//        mLayoutManager.setMoveSpeed(moveSpeed)
//    }
//
//    //设置图片间距
//    fun setItemSpace(itemSpace: Int) {
//        this.itemSpace = itemSpace
//        mLayoutManager.setItemSpace(itemSpace)
//    }
//
//    /**
//     * 设置轮播间隔时间
//     *
//     * @param autoPlayDuration 时间毫秒
//     */
//    fun setAutoPlayDuration(autoPlayDuration: Int) {
//        this.autoPlayDuration = autoPlayDuration
//    }
//
//    fun setOrientation(orientation: Int) {
//        mLayoutManager.orientation = orientation
//    }
//
//
//    /**
//     * 设置轮播数据集
//     */
//    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
//        hasInit = false
//        mRecyclerView.adapter = adapter
//        bannerSize = adapter.itemCount
//        mLayoutManager.infinite = bannerSize >= 2
//        isPlaying = true
//        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                this@BannerLayout.isPlaying = dx == 0
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                val first = mLayoutManager.getCurrentPosition()
//                if (currentIndex != first) {
//                    currentIndex = first
//                }
//                if (newState == SCROLL_STATE_IDLE) {
//                    isPlaying = true
//                }
//                refreshIndicator()
//            }
//        })
//        hasInit = true
//    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> isPlaying = false
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> isPlaying = true
//        }
//        return super.dispatchTouchEvent(ev)
//    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        isPlaying = true
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        isPlaying = false
//    }
//
//    override fun onWindowVisibilityChanged(visibility: Int) {
//        super.onWindowVisibilityChanged(visibility)
//        isPlaying = visibility == View.VISIBLE
//    }
//
//    /**
//     * 标示点适配器
//     */
//    internal inner class IndicatorAdapter : RecyclerView.Adapter<ViewHolder>() {
//        var currentPosition = 0
//        fun setPosition(currentPosition: Int) {
//            this.currentPosition = currentPosition
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val bannerPoint = ImageView(context)
//            val lp = RecyclerView.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin)
//            bannerPoint.layoutParams = lp
//            return object : RecyclerView.ViewHolder(bannerPoint) { }
//        }
//
//        override fun onBindViewHolder(holder:ViewHolder, position: Int) {
//            val bannerPoint = holder.itemView as ImageView
//            bannerPoint.setImageDrawable(if (currentPosition == position) mSelectedDrawable else mUnselectedDrawable)
//        }
//        override fun getItemCount(): Int {
//            return bannerSize
//        }
//    }
//
//    private fun dp2px(dp: Int): Int {
//        return TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
//            Resources.getSystem().displayMetrics
//        ).toInt()
//    }
//
//    /**
//     * 改变导航的指示点
//     */
//    @Synchronized
//    private fun refreshIndicator() {
//        if (showIndicator && bannerSize > 1) {
//            indicatorAdapter?.setPosition(currentIndex % bannerSize)
//            indicatorAdapter?.notifyDataSetChanged()
//        }
//    }
//}
//
//interface OnBannerItemClickListener {
//    fun onItemClick(position: Int)
//}
//
// interface OnPageChangeListener {
//    fun onPageSelected( position:Int)
//    fun onPageScrollStateChanged(state:Int)
//}
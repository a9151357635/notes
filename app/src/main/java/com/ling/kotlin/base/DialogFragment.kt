package com.ling.kotlin.base

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ling.kotlin.R
import com.ling.kotlin.common.CommonDialogEntity
import com.ling.kotlin.common.NoticeEntity
import com.ling.kotlin.utils.DateUtils
import com.ling.kotlin.utils.DialogUtils
import com.ling.kotlin.utils.DisplayUtils
import kotlinx.android.synthetic.main.base_dialog_layout.view.*
import kotlinx.android.synthetic.main.notice_dialog_layout.view.*


/**
 * Created by Administrator on 2017/8/9 0009.
 */

abstract class BaseDialogFragment : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //设置dialog的 进出 动画
        dialog?.window?.setWindowAnimations(R.style.AnimBottom)
        val view = inflater.inflate(layoutId, container, false)
        initView(view)
        return view
    }
    protected abstract val layoutId: Int
    open val isWindowTransparent: Boolean = false
    open val gravity: Int = Gravity.CENTER
    open val windowWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    open val windowHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    protected abstract fun initView(v: View)

    override fun onStart() {
        super.onStart()
        if (!isAdded) {
            return
        }
        setWindowWidth()
        if (isWindowTransparent) {
            setWindowTransparent()
        }
    }

    private fun setWindowTransparent() {
        val window = dialog?.window
        val windowParams = window?.attributes
        windowParams?.dimAmount = 0.0f
        window?.attributes = windowParams
    }

    fun setWindowWidth() {

        val win = dialog?.window
        // 一定要设置Background，如果不设置，window属性设置无效
        win!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//设置内容部分透明
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)

        val params = win.attributes
        params.gravity = gravity
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = windowWidth
        params.height = windowHeight
        win.attributes = params
    }

    fun showDialog(clz: DialogFragment, tag: String) {
        DialogUtils.showDialog(clz,childFragmentManager,tag)
    }
}

class NoticeDialog( override val layoutId: Int = R.layout.notice_dialog_layout) : BaseDialogFragment(), View.OnClickListener {
    private var noticeSize = 0
    private lateinit var mTitleTv: TextView
    private lateinit var mContentTv: TextView
    private lateinit var mTimeTv: TextView
    private lateinit var previousTv: TextView
    private lateinit  var nextTv: TextView
    private var dataList: List<NoticeEntity>? = null
     override val windowWidth: Int
        get() = (DisplayUtils.getWindowWidth(activity) / 1.4).toInt()
    override fun initView(v: View) {
        mTitleTv = v.notice_title_tv
        mContentTv = v.notice_content_tv
        mTimeTv = v.notice_time_tv
        v.notice_close_iv.setOnClickListener(this)
        previousTv = v.notice_previous_tv
        previousTv.setOnClickListener(this)
        nextTv = v.notice_next_tv
        nextTv.setOnClickListener(this)
        dataList = arguments?.getParcelableArrayList("notices")
        updateUI()
        if (!dataList.isNullOrEmpty()) {
            previousTv.setTextColor(ContextCompat.getColor(activity!!, R.color.app_text_three_normal_color))
            nextTv.setTextColor(ContextCompat.getColor(activity!!, R.color.app_fount_color))
        }
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.notice_close_iv -> dismissAllowingStateLoss()
            R.id.notice_previous_tv -> {
                if (0 != noticeSize) {
                    noticeSize--
                    setTextColor(noticeSize > 0, false)
                    updateUI()
                }
            }
            R.id.notice_next_tv -> {
                if (noticeSize == dataList?.size?.minus(1)) {
                    setTextColor(true, true)
                    return
                }
                noticeSize++
                setTextColor(noticeSize > 0, false)
                updateUI()
            }
        }

    }

    private fun setTextColor(isNext: Boolean, isLast: Boolean) {
        activity?.let {
            if(isNext){
                previousTv.setTextColor(ContextCompat.getColor(it, R.color.app_fount_color))
                nextTv.setTextColor(ContextCompat.getColor(it, if (isLast) R.color.app_text_three_normal_color else R.color.app_fount_color)
                )
            }else{
                previousTv.setTextColor(ContextCompat.getColor(it, R.color.app_text_three_normal_color))
                nextTv.setTextColor(ContextCompat.getColor(it, R.color.app_fount_color))
            }
        }
    }

    private fun updateUI() {
        dataList?.let {
            val (_, title, noticeContent, createTime) = it[noticeSize]
            mTitleTv.text = title
            mContentTv.text = noticeContent
            mTimeTv.text = createTime.replace("T"," ",true)
        }
    }
}

/**
 * 公共对话框
 */
class CommonDialog(private val dialogListener: DialogListener?=null,override val layoutId: Int = R.layout.base_dialog_layout) :BaseDialogFragment() {
    override val windowWidth: Int
        get() = (DisplayUtils.getWindowWidth(activity) / 1.4).toInt()
    override fun initView(v: View) {
        val dialogEntity = arguments?.getParcelable("dialogEntity") as? CommonDialogEntity ?: return
        v.dialog_cancel_tv.visibility = if(dialogEntity.isShowCancel) View.VISIBLE else View.GONE
        v.dialog_line.visibility = if(dialogEntity.isShowCancel) View.VISIBLE else View.GONE
        if(dialogEntity.isShowInput){
            v.dialog_content_et.visibility = View.VISIBLE
            v.dialog_title_tv.visibility = View.GONE
            v.dialog_content_tv.visibility = View.GONE
        }else{
            v.dialog_content_et.visibility = View.GONE
            v.dialog_title_tv.visibility = View.VISIBLE
            v.dialog_content_tv.visibility = View.VISIBLE
        }
        v.dialog_title_tv.text = if(dialogEntity.title.isNullOrEmpty()) context?.getString(R.string.dialog_title_tips) else dialogEntity.title
        v.dialog_content_et.hint = dialogEntity.inputHint
        v.dialog_cancel_tv.text = if(dialogEntity.cancelHint.isNullOrEmpty())  context?.getString(R.string.app_cancel_hint) else dialogEntity.cancelHint
        v.dialog_ok_tv.text = if(dialogEntity.sureHint.isNullOrEmpty())  context?.getString(R.string.app_sure_hint) else dialogEntity.sureHint
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            v.dialog_content_tv.text = Html.fromHtml(dialogEntity.content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            v.dialog_content_tv.text = Html.fromHtml(dialogEntity.content)
        }
        v.dialog_cancel_tv.setOnClickListener {
            dismissAllowingStateLoss()
        }
        v.dialog_ok_tv.setOnClickListener {
            dismissAllowingStateLoss()
            dialogListener?.onClick(v.dialog_content_et.text.toString())
        }
    }
    interface DialogListener{
        fun onClick(content:String)
    }
}
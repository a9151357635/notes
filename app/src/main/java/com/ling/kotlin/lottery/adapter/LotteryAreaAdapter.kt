package com.ling.kotlin.lottery.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.LotteryGroupInfoEntity
import com.ling.kotlin.lottery.bean.LotteryInfoEntity
import com.ling.kotlin.lottery.utils.LotteryId
import com.ling.kotlin.lottery.utils.LotteryUtils
import com.ling.kotlin.utils.ToastUtils
import com.ling.kotlin.widget.GridItemDecoration
import java.util.regex.Pattern

class LotteryAreaAdapter(dataList: List<LotteryGroupInfoEntity>?) :
    BaseQuickAdapter<LotteryGroupInfoEntity, BaseViewHolder>(R.layout.lottery_area_item, dataList) {
    private val selectList = mutableListOf<LotteryInfoEntity>()
    override fun setNewData(data: List<LotteryGroupInfoEntity>?) {
        selectList.clear()
        super.setNewData(data)
    }
    override fun convert(helper: BaseViewHolder, item: LotteryGroupInfoEntity) {
        val nameTv = helper.getView<TextView>(R.id.lottery_area_item_type_tv)
        nameTv?.text = item.name
        val mContentRv = helper.getView<RecyclerView>(R.id.lottery_area_item_content_rv)
        mContentRv.layoutManager = if (item.itemType == LotteryGroupInfoEntity.MARK_SIX) LinearLayoutManager(
            mContext,
            RecyclerView.VERTICAL,
            false
        ) else GridLayoutManager(mContext, if (item.lotteryId in LotteryUtils.sixMarkIdList) 3 else 2)
        mContentRv.addItemDecoration(GridItemDecoration(mContext, 1, R.color.app_e1e1e2_color))
        val contentAdapter = when {
            item.itemType == LotteryGroupInfoEntity.MARK_SIX -> AreaMarkSixAdapter(null)
            item.itemType == LotteryGroupInfoEntity.K_THREE -> AreaKThreeAdapter(null)
            else -> AreaInfoAdapter(null, item.itemType)
        }
        contentAdapter.setNewData(item.datas)
        mContentRv.adapter = contentAdapter
        contentAdapter.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.getItem(position) as LotteryInfoEntity
            if (isSelectEntity(entity)) {
                adapter.notifyItemChanged(position)
                //重新设置赔率
                if(entity.playId in LotteryId.markSixNotInPlayId || entity.playId in LotteryId.welfareThreePlayId || entity.playId in LotteryId.welfareSixPlayId){
                    nameTv?.text = item.name + if(!selectList.isNullOrEmpty())"(赔率:${entity.convertOdds})" else ""
                }
                onItemClickListener?.onItemClick(adapter, view, position)
            }
        }
    }

    /**
     * 是否选择或者删除Item
     */
    private fun isSelectEntity(entity: LotteryInfoEntity): Boolean {
        if (entity.selectId == 0) {
            //最大值判断
            if(LotteryId.maxSelectMap.any { (key,value) ->selectList.size == key && entity.playId in value }){
                ToastUtils.showToast(msg = mContext.getString(R.string.lottery_select_max_hint, selectList.size))
                return false
            }
            //11选五直选系列
            if (entity.playId in LotteryId.straightPlayId && selectList.any { entity.number == it.number }) {
                ToastUtils.showToast(msg = mContext.getString(R.string.lottery_select_duplicate_hint))
                return false
            }
            entity.selectId = 1
            selectList.add(entity)
        } else {
            entity.selectId = 0
            selectList.remove(entity)
        }
        entity.convertOdds = convertOdds(entity.playId)
        return true
    }

    /**
     * 转换赔率
     */
    private fun convertOdds(playId:String):String{
        return when (selectList.size) {
            4 -> {
                 when (playId) {
                     in LotteryId.welfareSixPlayId -> "32.5"
                    else -> "--"
                }
            }
            5 -> {
                when (playId) {
                    in LotteryId.welfareThreePlayId -> "12.95"
                    in LotteryId.welfareSixPlayId -> "13"
                    else -> "2.12"
                }
            }
            6 -> {
                when (playId) {
                    in LotteryId.welfareThreePlayId -> "8.61"
                    in LotteryId.welfareSixPlayId -> "6.5"
                    else -> "2.63"
                }
            }
            7 -> when (playId) {
                in LotteryId.welfareThreePlayId -> "6.15"
                in LotteryId.welfareSixPlayId -> "3.7"
                else -> "3.18"
            }
            8 -> when (playId) {
                in LotteryId.welfareThreePlayId -> "4.61"
                in LotteryId.welfareSixPlayId -> "2.35"
                else -> "3.72"
            }
            9 -> {
                when (playId) {
                    in LotteryId.welfareThreePlayId -> "3.6"
                    else -> "4.5"
                }
            }
            10 -> when (playId) {
                in LotteryId.welfareThreePlayId -> "2.87"
                else -> "5.58"
            }
            11 -> "6.8"
            12 -> "8.5"
            else -> "--"
        }
    }
}

class AreaInfoAdapter(dataList: List<LotteryInfoEntity>?, private val menuType: Int) :
    BaseQuickAdapter<LotteryInfoEntity, BaseViewHolder>(R.layout.lottery_area_info_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: LotteryInfoEntity) {
        val nameTv = helper.getView<TextView>(R.id.lottery_area_item_info_name_tv)
        val oddsTv = helper.getView<TextView>(R.id.lottery_area_item_info_odds_tv)
        if (item.isNumber == 0) {
            nameTv.text = item.name
            nameTv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        } else {
            nameTv.text = item.number.toString()
            nameTv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
        }
        oddsTv.text = item.odds
        oddsTv.visibility = if ("0" == item.odds) View.GONE else View.VISIBLE
        when (menuType) {
            LotteryGroupInfoEntity.NUMBER -> when {
                item.lotteryId in listOf(50, 55, 80, 81) -> {
                    nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                    LotteryUtils.numberMap[item.number]?.let { nameTv.setBackgroundResource(it) }
                }
                95 == item.lotteryId -> {
                    nameTv.text = ""
                    LotteryUtils.ballMap[item.number]?.let { nameTv.setBackgroundResource(it) }
                }
                else -> {
                    nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.app_text_main_another_normal_color))
                    nameTv.setBackgroundResource(R.drawable.ic_circle_white)
                }
            }
            LotteryGroupInfoEntity.SFY -> {
                if (item.isNumber == 1) {
                    nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                   val color =  LotteryUtils.colorMap[item.color]
                    if (color != null) {
                        nameTv.setBackgroundResource(color)
                    }else{
                        nameTv.setTextColor(ContextCompat.getColor(mContext,R.color.app_text_main_another_normal_color) )
                        nameTv.setBackgroundResource(R.drawable.ic_circle_white)
                    }
                }else{
                    nameTv.setBackgroundColor( ContextCompat.getColor(mContext,android.R.color.transparent))
                }
            }
        }
        BackgroundColorUtils.changeColor(mContext, helper.itemView, nameTv, oddsTv, item.selectId, item.isNumber)
    }
}

class AreaMarkSixAdapter(dataList: List<LotteryInfoEntity>?) :
    BaseQuickAdapter<LotteryInfoEntity, BaseViewHolder>(R.layout.lottery_area_info_marksix_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: LotteryInfoEntity) {
        val nameTv = helper.getView<TextView>(R.id.area_info_marksix_name_tv)
        val oddsTv = helper.getView<TextView>(R.id.area_info_marksix_odds_tv)
        val numberRecyclerView = helper.getView<RecyclerView>(R.id.area_info_marksix_content_rv)
        nameTv.text = item.name
        oddsTv.text = item.odds
        numberRecyclerView.adapter = AreaMarkSixContentAdapter(item.color.split(","), item.belongNumbers.split(","))
        BackgroundColorUtils.changeColor(mContext, helper.itemView, nameTv, oddsTv, item.selectId, item.isNumber)
    }
}

class AreaMarkSixContentAdapter(
    private val colors: List<String>,
    dataList: List<String>?
) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.lottery_area_info_marksix_content_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        val numberTv = helper.getView<TextView>(R.id.area_info_marksix_content_tv)
        numberTv.text = item
        val position = helper.layoutPosition
        LotteryUtils.colorMap[colors[position]]?.let {
            numberTv.setBackgroundResource(it)
        }
    }
}

class AreaKThreeAdapter(dataList: List<LotteryInfoEntity>?) :
    BaseQuickAdapter<LotteryInfoEntity, BaseViewHolder>(R.layout.lottery_area_info_kthree_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: LotteryInfoEntity) {
        val nameTv = helper.getView<TextView>(R.id.area_info_item_kthree_name_tv)
        val oddsTv = helper.getView<TextView>(R.id.area_info_item_kthree_odds_tv)
        val numberRecyclerView = helper.getView<RecyclerView>(R.id.area_info_item_kthree_rv)
        oddsTv.text = item.odds
        if (item.isNumber == 1) {
            nameTv.text = ""
            nameTv.visibility = View.GONE
            numberRecyclerView.visibility = View.VISIBLE
            numberRecyclerView.adapter = AreaKThreeNumberAdapter(nameToNumber(item.name))
        } else {
            nameTv.text = item.name
            nameTv.visibility = View.VISIBLE
            numberRecyclerView.visibility = View.GONE
        }
        BackgroundColorUtils.changeColor(mContext, helper.itemView, nameTv, oddsTv, item.selectId, item.isNumber)
    }

    /**
     * 名字转拆分转数组
     * @return
     */
    private fun nameToNumber(name: String): MutableList<String> {
        val names = mutableListOf<String>()
        if (Pattern.compile("[0-9]*").matcher(name).matches()) {
            val c = name.toCharArray()
            c.forEach {
                names.add(it.toString())
            }
        }
        return names
    }
}

class AreaKThreeNumberAdapter(dataList: List<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.lottery_area_info_kthree_number_item, dataList) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        LotteryUtils.dicemap[item?.toInt()]?.let { helper?.setBackgroundRes( R.id.kthree_number_tv, it) }
    }
}

object BackgroundColorUtils {
    /**
     * 设置选中的背景颜色 投注区域大量使用
     * @param mContext 上下文
     * @param selectId 是否选中
     * @param view 需要更改背景视图
     * @param mOddsTv 赔率
     * @param mNameTv 名字
     * @param isNumber 类型，是否需要改变名字的颜色 当是1的时候改变
     */
    fun changeColor(mContext: Context, view: View, mNameTv: TextView, mOddsTv: TextView, selectId: Int, isNumber: Int) {
        if (selectId == 1) {
            view.setBackgroundResource(R.drawable.shape_red_bg_drawale)
            mOddsTv.setTextColor(ContextCompat.getColor(mContext, R.color.app_lottery_text_select_color))
            if (0 == isNumber) mNameTv.setTextColor( ContextCompat.getColor(mContext,R.color.app_lottery_text_select_color))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent))
            mOddsTv.setTextColor(ContextCompat.getColor(mContext, R.color.app_fount_color))
            if (0 == isNumber) mNameTv.setTextColor( ContextCompat.getColor(mContext,R.color.app_text_main_another_normal_color)
            )
        }
    }
}
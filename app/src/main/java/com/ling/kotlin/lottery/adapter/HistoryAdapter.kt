package com.ling.kotlin.lottery.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.HistoryEntity
import com.ling.kotlin.lottery.utils.LotteryUtils

/**
 * 开奖历史适配器
 */
class LotteryHistoryAdapter(data:List<HistoryEntity>?):BaseQuickAdapter<HistoryEntity,BaseViewHolder>(R.layout.lottery_history_item,data){
    override fun convert(helper: BaseViewHolder, item: HistoryEntity) {
        helper.setText(R.id.lottery_history_periods_tv,mContext.getString(R.string.lottery_period_hint,item.expectNumber.toString()))
        helper.setText(R.id.lottery_history_time_tv,item.convertOpenTime)
        val openNumberRv = helper.getView<RecyclerView>(R.id.lottery_history_number_rv)
        val contentRv = helper.getView<RecyclerView>(R.id.lottery_content_rv)
        val lotteryId = item.lotteryID
        contentRv.visibility = View.GONE
        openNumberRv.layoutManager = if(65 == lotteryId) GridLayoutManager(mContext,11) else LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false)
        openNumberRv.adapter = LotteryOpenNumberAdapter(item.convertNumbers,item,false)
        val convertContent = if(lotteryId in LotteryUtils.sixMarkIdList) item.convertContent else LotteryUtils.openNumberConvertContent(lotteryId,item.convertNumbers)!!
        convertContent?.let {
            contentRv.visibility = View.VISIBLE
            contentRv.adapter = LotteryOpenNumberAdapter(it,item,true)
        }
    }
}

class LotteryOpenNumberAdapter(openNumberList:List<String>,private val entity: HistoryEntity,private val isOnlyContent:Boolean):BaseQuickAdapter<String,BaseViewHolder>(R.layout.lottery_history_open_number_item,openNumberList){

    override fun convert(helper: BaseViewHolder, item: String?) {
        val numberTv = helper.getView<TextView>(R.id.open_number_tv)
        val addTv = helper.getView<TextView>(R.id.number_add_tv)
         numberTv.text = item
        addTv.visibility = View.GONE
        val position = helper.layoutPosition
        val lastPosition = data.size -1
        if(isOnlyContent){
            numberTv.textSize = 14f
            numberTv.setTextColor(ContextCompat.getColor(mContext,R.color.app_text_main_another_normal_color))
            numberTv.setBackgroundResource(R.drawable.ic_number_zero)
            addTv.visibility =  if(entity.lotteryID in LotteryUtils.sixMarkIdList && position == lastPosition) View.INVISIBLE else View.GONE
            return
        }

        when (entity.lotteryID) {
            in LotteryUtils.pkTenSeriesIdList -> LotteryUtils.numberMap[item?.toInt()]?.let {numberTv.setBackgroundResource(it)}
            95 -> {
                numberTv.text = ""
                LotteryUtils.ballMap[item?.toInt()]?.let {numberTv.setBackgroundResource(it)}
            }
            in LotteryUtils.constantlySeriesIdList -> numberTv.setBackgroundResource(R.drawable.ic_circle_blue)
            in LotteryUtils.pcAgeSeriesIdList, in LotteryUtils.welfareSeriesIdList -> numberTv.setBackgroundResource(R.drawable.ic_circle_yellow)
            in LotteryUtils.kThreesSeriesIdList -> {
                numberTv.text = ""
                LotteryUtils.dicemap[item?.toInt()]?.let {numberTv.setBackgroundResource(it)}
            }
            65 -> numberTv.setBackgroundResource(R.drawable.ic_circle_red)
            in LotteryUtils.elevenIdList -> numberTv.setBackgroundResource(R.drawable.ic_circle_orange)
            in LotteryUtils.sixMarkIdList ->{
                addTv.visibility = if(position == lastPosition)View.VISIBLE else View.GONE
                entity.convertColor?.let {colors ->
                    LotteryUtils.colorMap[colors[position]]?.let {numberTv.setBackgroundResource(it)}
                }

            }
        }
    }
}
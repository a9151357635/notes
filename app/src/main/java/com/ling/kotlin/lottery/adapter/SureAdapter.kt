package com.ling.kotlin.lottery.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.LotteryInfoEntity
import com.ling.kotlin.lottery.bean.SureInfoEntity

/**
 * 正常投注适配器
 */
class LotterySureAdapter(dataList:List<LotteryInfoEntity>?):BaseQuickAdapter<LotteryInfoEntity,BaseViewHolder>(R.layout.lottery_bet_sure_dialog_item,dataList) {
    private var money:String?= null
    fun setMoney(money:String?){
        this.money = money?:"0"
    }
    override fun convert(helper: BaseViewHolder, item: LotteryInfoEntity) {
        val nameTv =  helper.getView<TextView>(R.id.lottery_sure_item_play_name_tv)
        val moneyTv = helper.getView<TextView>(R.id.lottery_sure_item_money_tv)
        helper.setText(R.id.lottery_sure_item_adds_tv,if(item.odds == "0") item.convertOdds else item.odds)
        moneyTv.text = mContext.getString(R.string.app_balance_hint,money).plus(if(item.type == LotteryInfoEntity.ONLY_ONE) "  共${if(0 == item.combineSize) 1 else item.combineSize}组" else "")
        helper.addOnClickListener(R.id.lottery_sure_item_close_tv)
        val convertName=  item.convertName
        if( convertName.isNullOrEmpty()){
            nameTv.text = item.remark
            return
        }
        nameTv.text = item.remark + convertName
    }
}

/**
 * 聊天室跟注适配器
 */
class ChatSureAdapter(dataList:List<SureInfoEntity>?):BaseQuickAdapter<SureInfoEntity,BaseViewHolder>(R.layout.lottery_bet_sure_dialog_item,dataList) {
    private var money:String?= null
    fun setMoney(money:String?){
        this.money = money?:"0"
    }
    override fun convert(helper: BaseViewHolder, item: SureInfoEntity) {
        helper.setText(R.id.lottery_sure_item_adds_tv,item.odds)
        helper.setText(R.id.lottery_sure_item_play_name_tv,item.playName)
        helper.setText(R.id.lottery_sure_item_money_tv, mContext.getString(R.string.app_balance_hint,money) + "  共${item.ZuShu}组")
        helper.addOnClickListener(R.id.lottery_sure_item_close_tv)
    }
}
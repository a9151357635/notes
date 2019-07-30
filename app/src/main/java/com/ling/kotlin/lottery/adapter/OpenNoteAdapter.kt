package com.ling.kotlin.lottery.adapter

import android.text.Html
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.OpenNoteInfoEntity
import com.ling.kotlin.lottery.utils.LotteryUtils
import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.utils.DateUtils

class OpenNoteAdapter(dataList: List<OpenNoteInfoEntity>?) :
    BaseQuickAdapter<OpenNoteInfoEntity, BaseViewHolder>(R.layout.lottery_open_note_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: OpenNoteInfoEntity) {
        helper.setImageResource(
            R.id.open_note_item_icon_iv,
            if (LotteryUtils.getAppIcon()[item.lotteryID] > 0) LotteryUtils.getAppIcon()[item.lotteryID] else R.mipmap.app_default_lotteryid_icon
        )
        helper.setText(R.id.open_note_item_name_tv,mContext.getString(R.string.lottery_bet_name_hint, item.lotteryName, item.periods))
        helper.setText(R.id.open_note_item_date_tv,DateUtils.convertTime(item?.betTime))
        val contentTv = helper.getView<TextView>(R.id.open_note_item_content_tv)
        val content = String.format(
            item.betOdds + " %s",
            "<font color='" + AppUtils.getAppColor() + "'>" + mContext.getString( R.string.lottery_bet_content_hint,item.betOdds,item.betMoney.toString()) + "</font>"
        )
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            contentTv.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            contentTv.text = Html.fromHtml(content)
        }
        val detailTv = helper.getView<TextView>(R.id.open_note_item_detail_tv)
        detailTv.visibility = if(item.detail.isNullOrEmpty()) View.VISIBLE else View.GONE
        detailTv.text = mContext.getString(R.string.lottery_bet_detail_hint,item.detail)
        //撤单
        helper.addOnClickListener(R.id.open_note_item_cancel_btn)
    }
}
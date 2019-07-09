package com.ling.kotlin.lottery.adapter

import android.view.View
import android.widget.TextView
import cn.iwgang.countdownview.CountdownView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.utils.LotteryUtils

class LotteryAdapter(data: List<LotteryEntity>?) :
    BaseQuickAdapter<LotteryEntity, BaseViewHolder>(R.layout.lottery_item, data) {

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        val stateTv = holder.getView<TextView>(R.id.lottery_state)
        val countdownView = holder.getView<CountdownView>(R.id.countdownView)
        val data = mData[holder.layoutPosition]
        handlerCountDownTimer(data, stateTv, countdownView)
    }

    override fun convert(helper: BaseViewHolder, item: LotteryEntity) {
        helper.setImageResource(R.id.lottery_iv, LotteryUtils.getAppIcon().get(item.lotteryId))
        helper.setText(R.id.lottery_name,item.lotteryName)
        helper.setImageResource(R.id.lottery_follow_iv,if(item.isFollow) R.drawable.ic_follow_select else R.drawable.ic_follow_normal)
        helper.addOnClickListener(R.id.lottery_follow_iv)
        val stateTv = helper.getView<TextView>(R.id.lottery_state)
        val countdownView = helper.getView<CountdownView>(R.id.countdownView)
        handlerCountDownTimer(item, stateTv, countdownView)
    }

    private fun handlerCountDownTimer(
        item: LotteryEntity,
        stateTv: TextView,
        countdownView: CountdownView
    ) {
        val remainTime = item.convertRemainTime - System.currentTimeMillis()
        if (remainTime > 0) {
            stateTv.visibility = View.GONE
            countdownView.visibility = View.VISIBLE
            countdownView.start(remainTime)
            countdownView.setOnCountdownEndListener {
                stateTv.visibility = View.VISIBLE
                it.visibility = View.GONE
            }
        } else {
            countdownView.stop()
            countdownView.allShowZero()
            countdownView.visibility = View.GONE
            stateTv.visibility = View.VISIBLE
        }
    }
}

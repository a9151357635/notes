package com.ling.kotlin.wallet.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.mikephil.charting.charts.LineChart
import com.ling.kotlin.R
import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.wallet.bean.ChartEntity
import com.ling.kotlin.wallet.utils.ChartManager

/**
 * 走势图适配器
 */
class ChartAdapter(data: List<ChartEntity>?) : BaseQuickAdapter<ChartEntity, BaseViewHolder>(R.layout.wallet_chart_item, data) {

    override fun convert(helper: BaseViewHolder, item: ChartEntity) {
        val chart = helper.getView<LineChart>(R.id.wallet_history_chart_item)
        val mTotalTv = helper.getView<TextView>(R.id.wallet_history_chart_item_total_tv)
        val mTypeTv = helper.getView<TextView>(R.id.wallet_history_chart_item_hint)
        mTypeTv.text = item.type
        ChartManager.setLineName(item.lastLineName)
        ChartManager.setLineName1(item.currentLineName)
        ChartManager.initDataStyle(item.maxRange, item.minRange, chart)
        ChartManager.setChartData(mContext, chart, item)
        mTotalTv.text = AppUtils.decimalFormat(item.total.toString())
        if (item.total <= 0) {
            mTotalTv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0)
        } else {
            mTotalTv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_up, 0, 0, 0)
        }
    }
}

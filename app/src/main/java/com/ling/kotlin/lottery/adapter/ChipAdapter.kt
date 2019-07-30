package com.ling.kotlin.lottery.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.ChipEntity

class ChipAdapter(dataList: List<ChipEntity>?) :
    BaseQuickAdapter<ChipEntity, BaseViewHolder>(R.layout.lottery_chip_item, dataList) {
    private var selectList: List<Int>? = null
    fun setSelectList(selectList: List<Int>) {
        this.selectList = selectList
    }

    override fun convert(helper: BaseViewHolder?, item: ChipEntity) {
        val numberTv = helper?.getView<TextView>(R.id.lottery_chips_tv)
        numberTv?.text = item.number.toString()
        if(selectList?.contains(item.number)!!){
            item.selectId = 1
            when (item.number) {
                1 -> {
                    numberTv?.setTextColor(Color.parseColor("#9B9F00"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_light_brown)
                }
                5 -> {
                    numberTv?.setTextColor(Color.parseColor("#329F00"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_light_green)
                }
                10 -> {
                    numberTv?.setTextColor(Color.parseColor("#A50065"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_brilliant_red)
                }
                50 -> {
                    numberTv?.setTextColor(Color.parseColor("#E74C3C"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_alizarin)
                }
                100 -> {
                    numberTv?.setTextColor(Color.parseColor("#B6121B"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_red)
                }
                500 -> {
                    numberTv?.setTextColor(Color.parseColor("#1D6CC1"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_light_blue)
                }
                1000 -> {
                    numberTv?.setTextColor(Color.parseColor("#26307D"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_dark_blue)
                }
                5000 -> {
                    numberTv?.setTextColor(Color.parseColor("#430CDA"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_purple)
                }
                10000 -> {
                    numberTv?.setTextColor(Color.parseColor("#5E9800"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_dark_green)
                }
                else -> {
                    numberTv?.setTextColor(Color.parseColor("#009688"))
                    numberTv?.setBackgroundResource(R.drawable.ic_chip_dark_brown)
                }
            }
        }else{
            item.selectId = 0
            numberTv?.setTextColor(Color.parseColor("#4C4C4C"))
            numberTv?.setBackgroundResource(R.drawable.ic_chip_normal)
        }
    }
}
package com.ling.kotlin.lottery.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.MenuChildEntity
import com.ling.kotlin.lottery.bean.MenuEntity


class MenuAdapter(dataList:List<MenuEntity>?) :BaseQuickAdapter<MenuEntity,BaseViewHolder>(R.layout.lottery_menu_item,dataList) {
    override fun convert(helper: BaseViewHolder?, item: MenuEntity?) {
        val menuNameTv = helper?.getView<TextView>(R.id.menu_name_tv)
        menuNameTv?.text = item?.menuName
        if(item?.selectId == 1){
            menuNameTv?.setTextColor(ContextCompat.getColor(mContext,R.color.white))
            menuNameTv?.setBackgroundColor(ContextCompat.getColor(mContext,R.color.app_fount_color))
        }else{
            menuNameTv?.setTextColor(ContextCompat.getColor(mContext,R.color.app_text_secondary_normal_color))
            menuNameTv?.setBackgroundColor(ContextCompat.getColor(mContext,android.R.color.transparent))
        }
    }
}

class MenuChildAdapter(dataList: List<MenuChildEntity>?):BaseQuickAdapter<MenuChildEntity,BaseViewHolder>(R.layout.lottery_menu_child_item,dataList) {
    override fun convert(helper: BaseViewHolder?, item: MenuChildEntity?) {
        val menuName =  item?.menuName?.split("-")?.get(0)?:item?.menuName
        helper?.setText(R.id.menu_child_item_tv,menuName)
        helper?.setVisible(R.id.menu_child_item_line,item?.selectId ==1)
    }
}
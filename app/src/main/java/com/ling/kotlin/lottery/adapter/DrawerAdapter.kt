package com.ling.kotlin.lottery.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.DrawerEntity
import com.ling.kotlin.lottery.bean.LotteryEntity

class DrawerAdapter(data:List<DrawerEntity>?) : BaseQuickAdapter<DrawerEntity,BaseViewHolder>(R.layout.bet_drawer_item,data){

    override fun convert(helper: BaseViewHolder, item: DrawerEntity) {
        val typeTv = helper.getView<TextView>(R.id.bet_drawer_type_tv)
        typeTv.text = item.typeName
        typeTv.setCompoundDrawablesWithIntrinsicBounds(if(item.type == 0) R.drawable.ic_follow_select else R.drawable.ic_hot,0,0,0)
        helper.setGone(R.id.bet_drawer_item_line, data.size > 1 && helper.layoutPosition == 0)
        val recyclerView = helper.getView<RecyclerView>(R.id.bet_drawer_item_rv)
        val adapter = DrawerChildAdapter(item.data)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            onItemClickListener?.onItemClick(adapter,view,position)
        }
    }

}
class DrawerChildAdapter(data:List<LotteryEntity>) : BaseQuickAdapter<LotteryEntity,BaseViewHolder>(R.layout.lottery_drawer_item_child,data){
    override fun convert(helper: BaseViewHolder, item: LotteryEntity) {
        helper.setText(R.id.drawer_child_name_tv,item.lotteryName)
    }

}
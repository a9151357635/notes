package com.ling.kotlin.lottery

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import com.ling.kotlin.lottery.adapter.LotteryAdapter
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.lottery.utils.LotteryEntityLiveData
import com.ling.kotlin.utils.CacheUtils
import kotlinx.android.synthetic.main.all_layout.view.*

class AllFragment(override val layoutId:Int = R.layout.all_layout) :BaseFragment(){
    private val adapter by lazy { LotteryAdapter(null) }
    private var followList:MutableList<Int>?= null
    override fun initView(v: View) {
        v.allRv.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.getItem(position) as LotteryEntity
            view?.let {
                Navigation.findNavController(it).navigate(R.id.action_lottery_to_betActivity,  bundleOf("lotteryId" to entity.lotteryId))
            }
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            val entity = adapter.getItem(position) as LotteryEntity
            when(view.id){
                R.id.lottery_follow_iv -> updateUI(entity)
            }
        }
        followList = CacheUtils.getFollowLottery()
    }

    private fun updateUI(item:LotteryEntity) {
        if(item.isFollow){
            followList?.remove(item.lotteryId)
            item.isFollow = false
        }else{
            followList?.add(item.lotteryId)
            item.isFollow = true
        }
        followList?.let { it1 -> CacheUtils.saveFollowLottery(it1) }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        setNewData()
    }

    private fun setNewData(){
        LotteryEntityLiveData.observe(this, Observer {
            adapter.setNewData(it)
        })
    }
}
package com.ling.kotlin.lottery

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import com.ling.kotlin.lottery.adapter.LotteryAdapter
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.lottery.viewmodel.LotteryViewModel
import com.ling.kotlin.utils.CacheUtils
import kotlinx.android.synthetic.main.collection_layout.view.*

class FollowFragment(override val layoutId:Int = R.layout.collection_layout) : BaseFragment() {

    private lateinit var adapter: LotteryAdapter
    private var followList:MutableList<Int>?= null
    override fun initView(v: View) {
        v.collectionRv.layoutManager = GridLayoutManager(context, 2)
        adapter = LotteryAdapter(null)
        adapter.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.getItem(position) as LotteryEntity
            view?.let {
                Navigation.findNavController(it).navigate(R.id.action_lottery_to_betActivity,  bundleOf("lotteryId" to entity.lotteryId,"lotteryName" to entity.lotteryName,"menuList" to entity.menuDetails))
            }
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            val entity = adapter.getItem(position) as LotteryEntity
            when(view.id){
                R.id.lottery_follow_iv -> updateUI(entity,position)
            }
        }
        v.collectionRv.adapter = adapter
        followList = CacheUtils.getFollowLottery()
    }

    private fun updateUI(item:LotteryEntity,position:Int) {
        if(item.isFollow){
            followList?.remove(item.lotteryId)
            item.isFollow = false
            adapter.remove(position)
        }
        followList?.let { it1 -> CacheUtils.saveFollowLottery(it1) }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        setNewData()
    }
    private fun setNewData(){
        parentFragment?.let { it ->
            val viewModel = ViewModelProviders.of(it).get(LotteryViewModel::class.java)
            viewModel.getLotteryEntitys(false).observe(it, Observer {
                adapter.setNewData(it.filter { lotteryEntity -> lotteryEntity.isFollow })
            })
        }
    }
}

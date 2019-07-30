package com.ling.kotlin.lottery

import android.content.Context
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import com.ling.kotlin.lottery.adapter.LotteryAreaAdapter
import com.ling.kotlin.lottery.adapter.MenuAdapter
import com.ling.kotlin.lottery.adapter.MenuChildAdapter
import com.ling.kotlin.lottery.bean.MenuChildEntity
import com.ling.kotlin.lottery.bean.MenuEntity
import com.ling.kotlin.lottery.utils.*
import com.ling.kotlin.lottery.viewmodel.LotteryViewModel
import kotlinx.android.synthetic.main.lottery_area_layout.view.*

/**
 * 投注区域
 */
class AreaFragment(override val layoutId: Int = R.layout.lottery_area_layout) :BaseFragment(){
    private val menuAdapter by lazy { MenuAdapter(null) }
    private val menuChildAdapter by lazy { MenuChildAdapter(null) }
    private val contentAdapter by lazy { LotteryAreaAdapter(null) }
    private var menuName:String? = null
    private var inductionUtils:InductionUtils?=null
    private val viewModel by lazy { getViewModel(LotteryViewModel::class.java) }
    override fun initView(v: View) {
        //夫menu
        v.menu_rv.adapter = menuAdapter
        menuAdapter.setOnItemClickListener { adapter, _, position ->
            val menuEntity = adapter.getItem(position) as MenuEntity
            setChildMenu(menuEntity)
            for(menuDetailEntity in adapter.data as List<MenuEntity>){
                menuDetailEntity.selectId = if(menuDetailEntity.id == menuEntity.id) 1 else 0
            }
            adapter.notifyDataSetChanged()
        }
        //子menu
        v.menu_child_rv.adapter = menuChildAdapter
        menuChildAdapter.setOnItemClickListener { adapter, _, position ->
            val childEntity  = adapter.getItem(position) as MenuChildEntity
            for(menuChildEntity in adapter.data as List<MenuChildEntity>){
                menuChildEntity.selectId = if(menuChildEntity.id == childEntity.id) 1 else 0
            }
            adapter.notifyDataSetChanged()
            getLotteryInfo(childEntity.lotteryId,childEntity.id)
        }
        //内容
        contentAdapter.setOnItemClickListener { _, _, _ ->
            //通知选中数据更新
            CombineEntityLiveData.postValue(contentAdapter.combineList)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //清空数据
        ClearLiveData.observe(this, Observer {
            contentAdapter.data.forEach{groupEntity ->groupEntity.datas.forEach {infoEntity -> infoEntity.selectId = 0 }}
            contentAdapter.setNewData(contentAdapter.data)
        })
        //菜单列表通知
        val menuLiveData = MediatorLiveData<List<MenuEntity>>()
        menuLiveData.addSource((activity as BetActivity).menus){
            menuLiveData.value =it
        }
        menuLiveData.observe(this, Observer {menus ->
            menus.forEachIndexed { index, menuEntity ->menuEntity.selectId = if(index ==0) 1 else 0}
            menuAdapter.setNewData(menus)
            setChildMenu(menus[0])
        })
        //随机选中的号码
        InductionLiveData.observe(this, Observer {
            //通知选中数据更新
            ClearLiveData.postValue(true)
            view?.menu_content_rv?.post {
                view?.menu_content_rv?.scrollToPosition(it)
                contentAdapter.setRandomPosition(it)
                contentAdapter.notifyDataSetChanged()
            }
        })
        IsInductionLiveData.observe(this, Observer{
            induction(it)
        })
    }

    private fun setChildMenu(menuEntity: MenuEntity){
        view?.menu_child_rv?.visibility = View.GONE
       val menuChildList = menuEntity.menuDetails
        this.menuName = menuEntity.menuName
        if(menuChildList.isNullOrEmpty()){
            getLotteryInfo(menuEntity.lotteryId,menuEntity.id)
            return
        }
        view?.menu_child_rv?.visibility = View.VISIBLE
        menuChildList.forEachIndexed { index, menuChildEntity -> menuChildEntity.selectId = if(index == 0)  1 else 0 }
        menuChildAdapter.setNewData(menuChildList)
        getLotteryInfo(menuEntity.lotteryId,menuChildList[0].id)
    }

    private fun getLotteryInfo(lotteryId:Int,menuId:String){
        //发送清空数据通知
        contentAdapter.data.forEach{groupEntity ->groupEntity.datas.forEach {infoEntity -> infoEntity.selectId = 0 }}
        contentAdapter.notifyDataSetChanged()
        menuName?.let {name ->
            viewModel.getLotteryInfo(lotteryId,menuId, name).observe(this, Observer {
                contentAdapter.setMenuId(menuId)
                contentAdapter.setNewData(it)
                view?.menu_content_rv?.adapter = contentAdapter
            })
        }
    }

    private fun induction(isInduction:Boolean){
        if(!isInduction){
            inductionUtils?.cancelShake()
            return
        }
        inductionUtils?.cancelShake()
        inductionUtils = InductionUtils(context,contentAdapter.data.size)
    }

    override fun onPause() {
        super.onPause()
        inductionUtils?.cancelShake()
    }
}
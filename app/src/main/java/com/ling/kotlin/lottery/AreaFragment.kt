package com.ling.kotlin.lottery

import android.view.View
import androidx.lifecycle.Observer
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import com.ling.kotlin.lottery.adapter.LotteryAreaAdapter
import com.ling.kotlin.lottery.adapter.MenuAdapter
import com.ling.kotlin.lottery.adapter.MenuChildAdapter
import com.ling.kotlin.lottery.bean.LotteryGroupInfoEntity
import com.ling.kotlin.lottery.bean.LotteryInfoEntity
import com.ling.kotlin.lottery.bean.MenuChildEntity
import com.ling.kotlin.lottery.bean.MenuEntity
import com.ling.kotlin.lottery.utils.LotteryId
import com.ling.kotlin.lottery.utils.LotteryUtils
import com.ling.kotlin.lottery.viewmodel.LotteryViewModel
import com.ling.kotlin.utils.ContextUtils
import kotlinx.android.synthetic.main.lottery_area_layout.view.*
import java.util.*

class AreaFragment(override val layoutId: Int = R.layout.lottery_area_layout) :BaseFragment(){
    private lateinit var menuAdapter:MenuAdapter
    private lateinit var menuChildAdapter: MenuChildAdapter
    private lateinit var contentAdapter:LotteryAreaAdapter
    private var menuname:String? = null
    private var menuId:String?= null
    private val viewModel by lazy { getViewModel(LotteryViewModel::class.java) }
    override fun initView(v: View) {
        menuAdapter = MenuAdapter(null)
        menuAdapter.setOnItemClickListener { adapter, _, position ->
            val menuEntity = adapter.getItem(position) as MenuEntity
            setChildMenu(menuEntity)
            for(menuDetailEntity in adapter.data as List<MenuEntity>){
                menuDetailEntity.selectId = if(menuDetailEntity.id == menuEntity.id) 1 else 0
            }
            adapter.notifyDataSetChanged()
        }
        v.menu_rv.adapter = menuAdapter
        menuChildAdapter = MenuChildAdapter(null)
        menuChildAdapter.setOnItemClickListener { adapter, _, position ->
            val childEntity  = adapter.getItem(position) as MenuChildEntity
            for(menuChildEntity in adapter.data as List<MenuChildEntity>){
                menuChildEntity.selectId = if(menuChildEntity.id == childEntity.id) 1 else 0
            }
            adapter.notifyDataSetChanged()
            getLotteryInfo(childEntity.lotteryId,childEntity.id)
        }
        v.menu_child_rv.adapter = menuChildAdapter
        contentAdapter = LotteryAreaAdapter(null)
        contentAdapter.setOnItemClickListener { adapter, _, _ ->
            val selectList = (adapter.data as List<LotteryInfoEntity>).filter { it.selectId ==1 }
            println("selectList== $selectList")
        }
    }

    override fun onStart() {
        super.onStart()
        getFromActivityData()
    }

    private fun getFromActivityData(){
        activity?.let {
            (it as BetActivity).menus.observe(this, Observer {menus ->
                menus.forEachIndexed { index, menuEntity ->menuEntity.selectId = if(index ==0) 1 else 0}
                menuAdapter.setNewData(menus)
               setChildMenu(menus[0])
            })
        }
    }

    private fun setChildMenu(menuEntity: MenuEntity){
        view?.menu_child_rv?.visibility = View.GONE
       val menuChildList = menuEntity.menuDetails
        this.menuname = menuEntity.menuName
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
        this.menuId = menuId
        menuname?.let {name ->
            viewModel.getLotteryInfo(lotteryId,menuId, name).observe(this, Observer {
                contentAdapter.setNewData(it)
                view?.menu_content_rv?.adapter = contentAdapter
            })
        }
    }

    private fun convertSelectList(selectList:List<LotteryInfoEntity>){
        val playId = selectList[0].playId
        val size =  when {
                LotteryId.leastOneList.any { it == playId } -> 1
                LotteryId.leastTwoList.any { it == playId || it == menuId } -> 2
                LotteryId.leastThreeList.any { it == playId || it == menuId } -> 3
                LotteryId.leastFourList.any { it == playId || it == menuId } -> 4
                LotteryId.leastFiveList.any { it == menuId } -> 5
                else -> 0
            }
    }
}
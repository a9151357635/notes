package com.ling.kotlin.lottery.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.ling.kotlin.lottery.bean.HistoryEntity
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.lottery.bean.LotteryGroupInfoEntity
import com.ling.kotlin.lottery.bean.PeriodTimeEntity
import com.ling.kotlin.lottery.repository.LotteryRepository
import com.ling.kotlin.retroft.viewmodel.BaseViewModel

class LotteryViewModel(application: Application) : BaseViewModel(application) {
    private val repository by lazy { LotteryRepository(this) }
    fun getActivity():LiveData<String> = repository.getActivity()
    fun getLotteryEntitys(refresh: Boolean): LiveData<List<LotteryEntity>> = repository.getLotteryRemoteEntitys(refresh)
    fun getCurPeriodTime(lotteryId:Int,type:Int):LiveData<PeriodTimeEntity> = repository.getCurPeriodTime(lotteryId, type)
    fun getLotteryHistorys(lotteryId: Int,param:Map<String,String>):LiveData<List<HistoryEntity>> = repository.getLotteryHistorys(lotteryId, param)
    fun getLotteryInfo(lotteryId: Int,menuId:String,menuName:String):LiveData<List<LotteryGroupInfoEntity>> = repository.getLotteryInfo(lotteryId, menuId,menuName)
}
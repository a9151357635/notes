package com.ling.kotlin.lottery.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.switchMap
import com.ling.kotlin.common.WalletBalanceEntity
import com.ling.kotlin.lottery.bean.*
import com.ling.kotlin.lottery.repository.LotteryRepository
import com.ling.kotlin.retroft.viewmodel.BaseViewModel

class LotteryViewModel(application: Application) : BaseViewModel(application) {
    private val repository by lazy { LotteryRepository(this) }
    fun getActivity():LiveData<String> = repository.getActivity()
    fun getLotteryEntitys(): LiveData<List<LotteryEntity>> = repository.getLotteryRemoteEntitys()
    fun getCurPeriodTime(lotteryId:Int,type:Int):LiveData<PeriodTimeEntity> = repository.getCurPeriodTime(lotteryId, type)
    fun getLotteryHistorys(lotteryId: Int,param:Map<String,String>):LiveData<List<HistoryEntity>> = repository.getLotteryHistorys(lotteryId, param)
    fun getLotteryInfo(lotteryId: Int,menuId:String,menuName:String):LiveData<List<LotteryGroupInfoEntity>> = repository.getLotteryInfo(lotteryId, menuId,menuName)
    fun getWalletBalance():LiveData<WalletBalanceEntity> = repository.getWalletBalance()
    fun betting(lotteryId: Int,param: Map<String, String>):LiveData<String> = repository.betting(lotteryId,param)
    fun openNoteEntity(lotteryId: Int,page:Int):LiveData<List<OpenNoteInfoEntity>> = repository.openNoteEntity(lotteryId, page)
    fun delOpenNoteEntity(id:String):LiveData<String> = repository.delOpenNoteEntity(id)
}
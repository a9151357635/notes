package com.ling.kotlin.lottery.repository

import com.ling.kotlin.lottery.bean.HistoryEntity
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.lottery.bean.LotteryGroupInfoEntity
import com.ling.kotlin.lottery.bean.PeriodTimeEntity
import com.ling.kotlin.retroft.BaseRemoteDataSource
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent


class LotteryRemoteData(baseViewModelEvent: IBaseViewModelEvent) : BaseRemoteDataSource(baseViewModelEvent){

    fun getAllLotteryEntity(callback: RequestCallback<List<LotteryEntity>>){
        executeQuietly(getService().getLotteryList(),callback)
    }
    fun getLotteryHistoryEntitys(lotteryId:Int,param:Map<String,String>,callback: RequestCallback<List<HistoryEntity>>){
        execute(getService().getLotteryHistoryList(lotteryId,param),callback)
    }
    fun getCurPeriodTime(lotteryId: Int,type:Int,callback: RequestCallback<PeriodTimeEntity>){
        executeQuietly(getService().getCurPeriodTime(lotteryId, type),callback)
    }
    fun getLotteryInfo(lotteryId: Int,menuId:String,callback: RequestCallback<List<LotteryGroupInfoEntity>>){
        execute(getService().getLotteryInfo(lotteryId, menuId),callback)
    }
    fun getActivity(callback: RequestCallback<String>){
        executeQuietly(getService().getActivity(),callback)
    }
}
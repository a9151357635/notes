package com.ling.kotlin.lottery.repository

import com.ling.kotlin.lottery.bean.HistoryEntity
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.retroft.BaseRemoteDataSource
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent


class LotteryRemoteData(baseViewModelEvent: IBaseViewModelEvent) : BaseRemoteDataSource(baseViewModelEvent){

    fun getAllLotteryEntity(callback: RequestCallback<List<LotteryEntity>>){
        executeQuietly(getService().getLotteryList(),callback)
    }
    fun getLotteryHistoryEntity(lotteryId:String,param:Map<String,String>,callback: RequestCallback<List<HistoryEntity>>){
        execute(getService().getLotteryHistoryList(lotteryId,param),callback)
    }
    fun getActivity(callback: RequestCallback<String>){
        execute(getService().getActivity(),callback)
    }
    fun getChessWithdrawAllUrl(callback: RequestCallback<String>){
        execute(getService().getChessWithdrawAllUrl(),callback)
    }
}
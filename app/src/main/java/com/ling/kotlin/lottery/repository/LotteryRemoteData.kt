package com.ling.kotlin.lottery.repository

import com.ling.kotlin.common.WalletBalanceEntity
import com.ling.kotlin.lottery.bean.*
import com.ling.kotlin.retroft.BaseRemoteDataSource
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.RequestMultiplayCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent
import com.ling.kotlin.wallet.bean.WalletMyEntity


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
    fun getWalletBalance(callback: RequestCallback<WalletBalanceEntity>){
        executeQuietly(getService().getWalletBalance(),callback)
    }
    fun betting(lotteryId:Int,param:Map<String,String>,callback: RequestCallback<String>){
        execute(getService().betting(lotteryId,param),callback)
    }
    fun openNoteEntity(param: Map<String, String>, callback: RequestCallback<OpenNoteEntity>){
        executeQuietly(getService().openNoteEntity(param),callback)
    }
    fun delOpenNoteEntity(id:String,callback: RequestCallback<String>){
        execute(getService().delOpenNoteEntity(id),callback)
    }
}
package com.ling.kotlin.lottery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.retroft.BaseException
import com.ling.kotlin.retroft.HttpConfig
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.RequestMultiplayCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent
import com.ling.kotlin.utils.CacheUtils
import com.ling.kotlin.utils.ToastUtils


class LotteryRepository(private val baseViewModelEvent: IBaseViewModelEvent){

    private val lotteryService = LotteryRemoteData(baseViewModelEvent)
    val lotteryList:MutableLiveData<List<LotteryEntity>> = MutableLiveData()
    val activityData:MutableLiveData<String> = MutableLiveData()
    val successData : MutableLiveData<String> = MutableLiveData()
    fun getLotteryRemoteEntitys(refresh: Boolean):LiveData<List<LotteryEntity>>{
        if(refresh){
            lotteryService.getAllLotteryEntity(object : RequestCallback<List<LotteryEntity>> {
                override fun onSuccess(data: List<LotteryEntity>) {
                    val followList = CacheUtils.getFollowLottery()
                    for(entity in data){
                        //根据服务器端返回的字段，换算成手机本地时间
                        val convertTime = entity.remainTime - entity.sysTime - entity.blockTime
                        if(convertTime > 0){
                            entity.convertRemainTime = convertTime * 1000 + System.currentTimeMillis()
                        }
                        if(!followList.isNullOrEmpty()){
                            //如果有关注，设置关注
                            if(followList.contains(entity.lotteryId)){
                                entity.isFollow = true
                            }
                        }
                    }
                    lotteryList.value = data
                }
            })
        }
        return lotteryList
    }

    fun getActivity():LiveData<String>{
        lotteryService.getActivity(object : RequestMultiplayCallback<String> {
            override fun onSuccess(data: String) {
                activityData.value = data
            }

            override fun onFail(e: BaseException) {
                if(e.code == HttpConfig.CODE_TOKEN_INVALID){
                    baseViewModelEvent.tokenInvalid()
                }else{
                    ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
                }
            }
        })
        return activityData
    }

    fun getChessWithdrawAllUrl():LiveData<String>{
        lotteryService.getChessWithdrawAllUrl(object : RequestMultiplayCallback<String> {
            override fun onSuccess(data: String) {
                successData.value = data
            }

            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
            }
        })
        return activityData
    }
     fun destroyDatabase(){
//        LotteryDatabase.destroyInstance()
    }
}

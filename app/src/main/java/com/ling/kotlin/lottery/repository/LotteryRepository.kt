package com.ling.kotlin.lottery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ling.kotlin.common.WalletBalanceEntity
import com.ling.kotlin.lottery.bean.*
import com.ling.kotlin.lottery.utils.CurPeriodNumLiveData
import com.ling.kotlin.lottery.utils.LotteryEntityLiveData
import com.ling.kotlin.lottery.utils.WalletBalanceLiveData
import com.ling.kotlin.retroft.BaseException
import com.ling.kotlin.retroft.HttpConfig
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.RequestMultiplayCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent
import com.ling.kotlin.utils.CacheUtils
import com.ling.kotlin.utils.ToastUtils


class LotteryRepository(private val baseViewModelEvent: IBaseViewModelEvent){

    private val lotteryService = LotteryRemoteData(baseViewModelEvent)
    val activityData:MutableLiveData<String> = MutableLiveData()
    val successData : MutableLiveData<String> = MutableLiveData()
    val preriodTimeEntity = MutableLiveData<PeriodTimeEntity>()
    val historyData = MutableLiveData<List<HistoryEntity>>()
    val lotteryInfoLiveData = MutableLiveData<List<LotteryGroupInfoEntity>>()
    val openNoteInfoLiveData = MutableLiveData<List<OpenNoteInfoEntity>>()
    val delOpenNoteLiveData = MutableLiveData<String>()
    private val doubleModules = listOf("双面玩法","冠亚组合","正特","前中后")
    private val numberModules = listOf("数字")
    private val markSixModules = listOf("一肖","特肖","尾数","特码头尾","连肖","连尾")
    private val kThreeModules = listOf("三军","围骰","点数","长牌","短牌","和值","三连号","三同号","二同号","跨度","牌点","不出号码","必出号码")
    fun getLotteryRemoteEntitys():LiveData<List<LotteryEntity>>{
        lotteryService.getAllLotteryEntity(object : RequestCallback<List<LotteryEntity>> {
            override fun onSuccess(data: List<LotteryEntity>) {
                val followList = CacheUtils.getFollowLottery()
                data.forEach {  convertLotteryTimeAttributes(it, followList) }
                LotteryEntityLiveData.postValue(data)
            }
        })
        return LotteryEntityLiveData
    }

    /**
     * 转换服务器时间为本地时间
     */
    private fun convertLotteryTimeAttributes(entity: LotteryEntity, followList: MutableList<Int> ) {
        //根据服务器端返回的字段，换算成手机本地时间
        val convertTime = entity.remainTime - entity.sysTime - entity.blockTime
        if (convertTime > 0) {
            entity.convertRemainTime = convertTime * 1000 + System.currentTimeMillis()
        }
        if (!followList.isNullOrEmpty()) {
            //如果有关注，设置关注
            if (entity.lotteryId in followList) {
                entity.isFollow = true
            }
        }
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

    fun getCurPeriodTime(lotteryId:Int,type:Int):LiveData<PeriodTimeEntity>{
        lotteryService.getCurPeriodTime(lotteryId,type,object :RequestCallback<PeriodTimeEntity>{
            override fun onSuccess(data: PeriodTimeEntity) {
                data.convertRemainTime = data.remainTime-data.sysTime - data.blockTime
                preriodTimeEntity.postValue(data)
                //通知期号变更
                CurPeriodNumLiveData.postValue(data.curPeriodNum)
            }
        })
        return preriodTimeEntity
    }

    fun getLotteryHistorys(lotteryId: Int,param:Map<String,String>):LiveData<List<HistoryEntity>>{

        lotteryService.getLotteryHistoryEntitys(lotteryId,param,object :RequestCallback<List<HistoryEntity>>{
            override fun onSuccess(data: List<HistoryEntity>) {
                data.forEach { convertEntityAttributes(it)}
                historyData.value = data
            }
        })
        return historyData
    }

    /**
     * 转换实体属性
     */
    private fun convertEntityAttributes(entity: HistoryEntity) {
        entity.openTime?.let { entity.convertOpenTime = if (it.contains("T")) it.replace("T", " ", true) else it }
        entity.openCode?.let {
            var resultList = it.split(",") as ArrayList
            val lastItem = resultList.removeAt(resultList.size - 1)
            if ("+" in lastItem) {
                resultList.addAll(listOf(*lastItem.split("\\+".toRegex()).dropLastWhile { its -> its.isEmpty() }.toTypedArray()))
                //最后的前一个加入要给识别
                resultList.add(resultList.size - 1, "+")
            } else {
                resultList.add(lastItem)
            }
            entity.convertNumbers = resultList
        }
        entity.resultColor?.let { entity.convertColor = it.split(",") }
        entity.zodiac?.let { entity.convertContent = it.split(",") }
    }

    fun getLotteryInfo(lotteryId: Int,menuId:String,menuName: String):LiveData<List<LotteryGroupInfoEntity>>{
        lotteryService.getLotteryInfo(lotteryId,menuId,object:RequestMultiplayCallback<List<LotteryGroupInfoEntity>>{
            override fun onFail(e: BaseException) {
//                if(e.code == HttpConfig.CODE_TOKEN_INVALID){
//                    baseViewModelEvent.tokenInvalid()
//                }else{
//                    ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
//                }
                ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
            }

            override fun onSuccess(data: List<LotteryGroupInfoEntity>) {
                data.forEach {
                    it.menuId = menuId
                    it.itemType = convertModel(menuName)
                }
                lotteryInfoLiveData.postValue(data)
            }
        })
        return lotteryInfoLiveData
    }

    private fun convertModel(menuName: String):Int{
        return when {
            doubleModules.contains(menuName) -> LotteryGroupInfoEntity.DOUBLE
            numberModules.contains(menuName) -> LotteryGroupInfoEntity.NUMBER
            markSixModules.contains(menuName) -> LotteryGroupInfoEntity.MARK_SIX
            kThreeModules.contains(menuName) -> LotteryGroupInfoEntity.K_THREE
            //亚冠和、特码、正码、正特、连码、自选不中和其他
            else -> LotteryGroupInfoEntity.SFY
        }
    }

    /**
     * 获取用户钱包余额
     */
    fun getWalletBalance():LiveData<WalletBalanceEntity>{
        lotteryService.getWalletBalance(object:RequestMultiplayCallback<WalletBalanceEntity>{
            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
            }

            override fun onSuccess(data: WalletBalanceEntity) {
                WalletBalanceLiveData.postValue(data)
            }
        })
        return WalletBalanceLiveData
    }

    fun betting(lotteryId: Int,map: Map<String, String>): LiveData<String>{

        lotteryService.betting(lotteryId,map,object:RequestMultiplayCallback<String>{
            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
            }

            override fun onSuccess(data: String) {
                successData.postValue(data)
            }
        })
        return successData
    }

    fun openNoteEntity(lotteryId: Int,page:Int):LiveData<List<OpenNoteInfoEntity>>{
        val map = mapOf("status" to "0","page" to page.toString(),"size" to "20","lotteryID" to lotteryId.toString())
        lotteryService.openNoteEntity(map,object:RequestMultiplayCallback<OpenNoteEntity>{
            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
            }

            override fun onSuccess(data: OpenNoteEntity) {
                openNoteInfoLiveData.postValue(data.items)
            }
        })
        return openNoteInfoLiveData
    }

    fun delOpenNoteEntity(id:String):LiveData<String>{
        lotteryService.delOpenNoteEntity(id,object:RequestMultiplayCallback<String>{
            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "${e.code} msg==${e.message}")
            }

            override fun onSuccess(data: String) {
                delOpenNoteLiveData.postValue(data)
            }
        })
        return delOpenNoteLiveData
    }
}

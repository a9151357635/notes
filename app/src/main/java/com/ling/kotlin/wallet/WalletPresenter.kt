package com.ling.kotlin.wallet

import android.content.Context
import android.text.TextUtils
import com.ling.kotlin.base.BasePresenter
import com.ling.kotlin.base.Callback
import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.utils.JSonUtils
import com.ling.kotlin.wallet.bean.ChartEntity
import com.ling.kotlin.wallet.bean.WalletEntity
import com.ling.kotlin.wallet.bean.WalletHistoryEntity
import com.ling.kotlin.wallet.bean.WalletMyEntity

class WalletPresenter : BasePresenter{

    private var context:Context?=null
    constructor(context: Context,callback: Callback):super(callback){
        this.context = context
    }

    fun getChartData(url:String){
        val result = AppUtils.getAssetStr("wallet_chart_data.json")
        if(TextUtils.isEmpty(result)){
            return
        }
        var chatEntitys = JSonUtils.parseArray(result, ChartEntity::class.javaObjectType)
        callback(chatEntitys,url)
    }

    fun  getWalletEntity(url: String){
        val  result = AppUtils.getAssetStr("wallet_entity_data.json")
        if(TextUtils.isEmpty(result)){
            return
        }
        var  walletEntity = JSonUtils.parseObject(result,WalletEntity::class.javaObjectType)
        callback(walletEntity,url)
    }

    fun getHistoryData(url:String){
        val result = AppUtils.getAssetStr("wallet_history_data.json")
        if(TextUtils.isEmpty(result)){
            return
        }
        var walletHistory = JSonUtils.parseArray(result, WalletHistoryEntity::class.javaObjectType)
        callback(walletHistory,url)
    }

    fun getMyWalletData(url: String){
        var  result = AppUtils.getAssetStr("wallet_my_data.json")
        if(TextUtils.isEmpty(result)){
            return
        }
        var walletMyEntity = JSonUtils.parseObject(result,WalletMyEntity::class.javaObjectType)
        callback(walletMyEntity,url)
    }
}
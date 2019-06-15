package com.ling.kotlin.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.wallet.bean.ChartEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.net.URL

class WalletRepository<T> {

    fun<T> getChartEntity(url:String):LiveData<T>{
//        var data = ChartEntity(emptyList() ,emptyList() ,0.0f,"")
        val dataList = MutableLiveData<T>()
//        var(currentMonths,lastMonths,total,type) = data
        doAsync{
//            val jsonStr = URL(url).readText()
            val jsonStr = AppUtils.getAssetStr("wallet_chart_data.json")
            var data = Gson().fromJson(Gson().toJson(jsonStr),ChartEntity::class.java);
            uiThread{
                dataList.value = data as T
            }
        }
        return dataList
    }


    fun<T> getChartEntityLocal(jsonStr:String):LiveData<T>{
        val dataList = MutableLiveData<T>()
        doAsync{
            var data = Gson().fromJson(Gson().toJson(jsonStr),ChartEntity::class.java)
            uiThread{
                dataList.value = data as T
            }
        }
        return dataList
    }
}
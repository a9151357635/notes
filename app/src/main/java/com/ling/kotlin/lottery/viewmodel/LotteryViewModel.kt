package com.ling.kotlin.lottery.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.lottery.repository.LotteryRepository
import com.ling.kotlin.retroft.viewmodel.BaseViewModel

class LotteryViewModel(application: Application) : BaseViewModel(application) {
    private val repository by lazy { LotteryRepository(this) }
    fun getLotteryEntitys(refresh: Boolean): LiveData<List<LotteryEntity>> {
        return repository.getLotteryRemoteEntitys(refresh)
    }

    fun getActivity():LiveData<String>{
        return repository.getActivity()
    }

    fun getChessWithdrawAllUrl():LiveData<String>{
        return repository.getChessWithdrawAllUrl()
    }

    fun destroyDatabase() {
//        repository.destroyDatabase()
    }
}
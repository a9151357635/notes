package com.ling.kotlin.lottery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ling.kotlin.login.entity.LoginEntity
import com.ling.kotlin.login.repository.LoginRemoteData
import com.ling.kotlin.retroft.BaseException
import com.ling.kotlin.retroft.RequestMultiplayCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent
import com.ling.kotlin.utils.CacheUtils
import com.ling.kotlin.utils.ToastUtils


class LoginRepository(private val baseViewModelEvent: IBaseViewModelEvent){

    private val loginService = LoginRemoteData(baseViewModelEvent)
    val serInfoLiveData  =  MutableLiveData<LoginEntity>()
    fun register(param:Map<String,Any>):LiveData<LoginEntity>{
        loginService.register(param,object :RequestMultiplayCallback<LoginEntity>{
            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "msg :${e.message},code = ${e.code}")
            }

            override fun onSuccess(data: LoginEntity) {
                CacheUtils.saveLoginInfo(data)
                serInfoLiveData.postValue(data)
            }
        })
        return serInfoLiveData
    }

    fun login(param:Map<String,Any>):LiveData<LoginEntity>{
        loginService.login(param,object :RequestMultiplayCallback<LoginEntity>{
            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "msg :${e.message},code = ${e.code}")
            }

            override fun onSuccess(data: LoginEntity) {
                CacheUtils.saveLoginInfo(data)
                serInfoLiveData.postValue(data)
            }
        })
        return serInfoLiveData
    }
}


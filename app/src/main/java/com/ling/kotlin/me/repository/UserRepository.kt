package com.ling.kotlin.me.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ling.kotlin.common.UserInfoLiveData
import com.ling.kotlin.me.entity.UserInfoEntity
import com.ling.kotlin.retroft.BaseException
import com.ling.kotlin.retroft.RequestMultiplayCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent
import com.ling.kotlin.utils.ToastUtils

class UserRepository(private val baseViewModelEvent: IBaseViewModelEvent) {
    private val loginService = UserRemoteData(baseViewModelEvent)
    val userInfoLiveData  =  MutableLiveData<UserInfoEntity>()

    fun findUserInfoEntity():LiveData<UserInfoEntity>{
        loginService.findUserInfoEntity(object: RequestMultiplayCallback<UserInfoEntity>{
            override fun onFail(e: BaseException) {
                ToastUtils.showToast(msg = "msg:${e.message},code:${e.code}")
            }

            override fun onSuccess(data: UserInfoEntity) {
                UserInfoLiveData.postValue(data)
                userInfoLiveData.postValue(data)
            }
        })
        return userInfoLiveData
    }
}
package com.ling.kotlin.me.repository

import com.ling.kotlin.me.entity.UserInfoEntity
import com.ling.kotlin.retroft.BaseRemoteDataSource
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent


class UserRemoteData(baseViewModelEvent: IBaseViewModelEvent) : BaseRemoteDataSource(baseViewModelEvent){
    /**
     * 查询用户信息
     */
    fun findUserInfoEntity( callback: RequestCallback<UserInfoEntity>){
        execute(getNewService().findUserInfoEntity(),callback)
    }

}
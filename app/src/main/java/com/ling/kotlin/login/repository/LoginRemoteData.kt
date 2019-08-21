package com.ling.kotlin.login.repository

import com.google.gson.Gson
import com.ling.kotlin.login.entity.LoginEntity
import com.ling.kotlin.retroft.BaseRemoteDataSource
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class LoginRemoteData(baseViewModelEvent: IBaseViewModelEvent) : BaseRemoteDataSource(baseViewModelEvent){

    fun register(param: Map<String, Any>, callback: RequestCallback<LoginEntity>){
        val requestBody = Gson().toJson(param).toRequestBody("application/json".toMediaTypeOrNull())
        execute(getNewService().register(requestBody),callback)
    }

    fun login(param: Map<String, Any>, callback: RequestCallback<LoginEntity>){
        val requestBody = Gson().toJson(param).toRequestBody("application/json".toMediaTypeOrNull())
        execute(getNewService().login(requestBody),callback)
    }
}
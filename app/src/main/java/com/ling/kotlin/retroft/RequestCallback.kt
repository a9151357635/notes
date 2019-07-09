package com.ling.kotlin.retroft

interface RequestCallback<T>{
    fun  onSuccess(data:T)
}

interface RequestMultiplayCallback<T>: RequestCallback<T> {
    
    fun onFail(e: BaseException)
}
package com.ling.kotlin.retroft

import android.util.Log
import io.reactivex.observers.DisposableObserver

class BaseSubscriber<T> constructor(private val requestCallback: RequestCallback<T>) :DisposableObserver<OptionT<T>>(){

    override fun onComplete() {

    }

    override fun onNext(t: OptionT<T>) {
       requestCallback.onSuccess(t.value)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        val msg = e.message ?: "未知错误"
        if(requestCallback is RequestMultiplayCallback){
            if(e is BaseException){
                requestCallback.onFail(e)
            }else{
                requestCallback.onFail(ServerResultException(msg))
            }
        }else{
            //TODO
            Log.e("BaseSubscriber","msg$msg")
//            ToastUtils.showToast(msg = msg)
        }
    }

}
package com.ling.kotlin.base

 abstract class BasePresenter {

    private var callback:Callback ?= null

    constructor(callback: Callback){
        this.callback = callback
    }

     fun callback(`object`: Any?, url: String) {
        if (`object` == null) {
            callback!!.onError("请刷新再试", -2, url)
        } else {
            if (`object` is List<*>) {
                if (`object`.size <= 0) {
                    callback!!.onError("请刷新再试", -2, url)
                    return
                }
            }
            callback!!.onSuccess(`object`, url)
        }
    }
}


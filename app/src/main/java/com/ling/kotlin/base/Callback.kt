package com.ling.kotlin.base

interface Callback {
    fun onSuccess(data: Any, url: String)
    fun onError(error: String, errorCode: Int, url: String)
}
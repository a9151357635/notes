package com.ling.kotlin.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel(){
    private var url = ""
    var data:LiveData<T>? = null

    fun init(url:String){
        this.url = url
    }
}
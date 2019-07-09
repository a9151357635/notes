package com.ling.kotlin.retroft.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface  IBaseViewModelEvent{
    fun showLoading(msg:String)

    fun showLoading(){
        showLoading("")
    }

    fun dismissLoading()

    fun showToast(msg: String)

    fun tokenInvalid()

    fun finishView()

    fun pop()
}

interface IBaseViewModelEventEventObserver : IBaseViewModelEvent {
    fun initViewModel(): BaseViewModel?{
        return null
    }

    fun initViewModelList():MutableList<BaseViewModel>?{
        return null
    }

    fun getLifecycleOwner():LifecycleOwner

    fun initViewModelEvent(){
        var list:MutableList<BaseViewModel>? = null
        val initViewModel = initViewModelList()
        if(initViewModel.isNullOrEmpty()){
            val baseViewModel = initViewModel()
            baseViewModel?.let {
                list = mutableListOf(it)
            }
        }else{
            list = initViewModel
        }
        list?.let {
            observeEvent(it)
        }
    }

    fun  observeEvent(viewModelList:MutableList<BaseViewModel>){
        for (viewModel in viewModelList){
            viewModel.baseActionEvent.observe(getLifecycleOwner(), Observer {it ->
                it?.let {
                    when(it.action){
                        BaseViewModelEvent.SHOW_LOADING_DIALOG -> showLoading(it.message)
                        BaseViewModelEvent.DISMISS_LOADING_DIALOG -> dismissLoading()
                        BaseViewModelEvent.SHOW_TOAST -> showToast(it.message)
                        BaseViewModelEvent.FINISH -> finishView()
                        BaseViewModelEvent.TOKEN_INVALID -> tokenInvalid()
                        BaseViewModelEvent.POP -> pop()
                    }
                }
            })
        }
    }
    fun  getLContext():Context?

    fun <T> startActivity(clz:Class<T>){
        getLContext()?.startActivity(Intent(getLContext(),clz))
    }
    override fun showToast(msg: String) {
        Toast.makeText(getLContext(),msg,Toast.LENGTH_SHORT).show()
    }
}
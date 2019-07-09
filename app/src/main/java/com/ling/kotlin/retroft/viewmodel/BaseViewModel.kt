package com.ling.kotlin.retroft.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

open class BaseViewModel(application: Application) : AndroidViewModel(application),
    IBaseViewModelEvent {
    val baseActionEvent = MutableLiveData<BaseViewModelEvent>()
    override fun showLoading(msg: String) {
        val event =
            BaseViewModelEvent(BaseViewModelEvent.SHOW_LOADING_DIALOG)
        event.message = msg
        baseActionEvent.value = event
    }

    override fun dismissLoading() {
        val event =
            BaseViewModelEvent(BaseViewModelEvent.DISMISS_LOADING_DIALOG)
        baseActionEvent.value = event
    }

    override fun showToast(msg: String) {
        val  event =
            BaseViewModelEvent(BaseViewModelEvent.SHOW_TOAST)
        event.message = msg
        baseActionEvent.value = event
    }

    override fun finishView() {
      val event =
          BaseViewModelEvent(BaseViewModelEvent.FINISH)
        baseActionEvent.value = event
    }

    override fun pop() {
        val event =
            BaseViewModelEvent(BaseViewModelEvent.POP)
        baseActionEvent.value = event
    }
    override fun tokenInvalid() {
        val event =
            BaseViewModelEvent(BaseViewModelEvent.TOKEN_INVALID)
        baseActionEvent.value = event
    }

}
package com.ling.kotlin.retroft.viewmodel


open class BaseEvent(open val action:Int)

class BaseViewModelEvent(override val action: Int): BaseEvent(action){

    companion object{
        const val SHOW_LOADING_DIALOG = 1
        const val DISMISS_LOADING_DIALOG = 2
        const val SHOW_TOAST = 3
        const val FINISH = 4
        const val POP = 5
        const val TOKEN_INVALID = 6
    }

    var message:String = ""
}
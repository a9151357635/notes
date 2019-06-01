package com.ling.kotlin

import androidx.multidex.MultiDexApplication

class LotteryApp : MultiDexApplication(){


    companion object {
        var instance:LotteryApp? = null
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance =  this
    }

}
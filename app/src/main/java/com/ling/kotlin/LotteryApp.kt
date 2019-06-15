package com.ling.kotlin

import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco

class LotteryApp : MultiDexApplication(){


    companion object {
        var instance:LotteryApp? = null
        private set
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        instance =  this
    }

}
package com.ling.kotlin

import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import io.paperdb.Paper

class LotteryApp : MultiDexApplication(){


    companion object {
       lateinit  var instance:LotteryApp
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance =  this
        Paper.init(this.getApplicationContext())
        Fresco.initialize(this)
    }

}
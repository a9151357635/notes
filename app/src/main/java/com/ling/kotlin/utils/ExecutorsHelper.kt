package com.ling.kotlin.utils


import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ExecutorsHelper {
    var disIoExecutor = Executors.newSingleThreadExecutor()
    var mainExecutor = MainExecutor()

    companion object{
        class MainExecutor : Executor{
            private val mHandler by lazy { Handler(Looper.getMainLooper()) }
            override fun execute(command: Runnable?) {
                mHandler.post(command)
            }
        }
    }
}
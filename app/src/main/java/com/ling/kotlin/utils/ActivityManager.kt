package com.ling.kotlin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.lang.Exception
import java.util.*

/****
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
class ActivityManager private constructor(){

    companion object {
        private var activityStack:Stack<Activity>?= null
        private var instance:ActivityManager? = null

        fun  getInstance():ActivityManager{
            if(instance == null){
                synchronized(ActivityManager::class.java){
                    instance = ActivityManager()
                }
            }
            return instance!!
        }
    }

    /**
     * 获取总Activity数量
     */
    val activitySize:Int
    get() =  activityStack!!.size

    /**
     * 添加activity到堆栈
     */
    fun  pushActivity(activity: Activity){
        if(activityStack == null){
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 删除activity到堆栈
     */
    fun popActivity(activity: Activity){
        activityStack!!.remove(activity)
    }

    /**
     * 获取当前Activity(堆栈中最后一个压入的)
     *
     * @return
     */
    fun currentActivity():Activity{
        return activityStack!!.lastElement()
    }

    /**
     * 获取activity
     *
     * @return
     */
    fun getActivity(cls:Class<*>):Activity?{
        for (activity in activityStack!!){
            if(activity.javaClass.simpleName == cls.simpleName){
                return activity
            }
        }
        return null
    }
    /**
     * 结束当前Activity(堆栈中最后一个压入的)
     */
    fun finishLastActivity(){
        val  activity = activityStack!!.lastElement()
        finishActivity(activity, Activity.RESULT_CANCELED)
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    private fun finishActivity(activity: Activity?,resultCode:Int){
        if(activity != null){
            activityStack!!.remove(activity)
            activity.setResult(resultCode)
            activity.finish()
        }
    }

    /**
     * 结束指定的activity
     */
   private fun finishActivity(activity: Activity?,resultRequest:Int,data:Intent){
       if(activity != null){
           activityStack!!.remove(activity)
           activity.setResult(resultRequest,data)
           activity.finish()
       }
   }
    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    fun finishActivity(cls: Class<*>){
        for (activity in activityStack!!){
            if(activity.javaClass.simpleName == cls.simpleName){
                finishActivity(activity,Activity.RESULT_CANCELED)
            }
        }
    }

    /**
     * 结束指定类名的activity
     */
    fun finishActivity(cla:Class<*>,resultRequest: Int,data: Intent){
        for (activity in activityStack!!){
            if(activity.javaClass.simpleName == cla.simpleName){
                finishActivity(activity, resultRequest, data)
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>,resultRequest: Int){
        for (activity in activityStack!!){
            if(activity.javaClass.simpleName == cls.simpleName){
                finishActivity(activity,resultRequest)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity(){
        var i = 0
        val size = activityStack!!.size
        while (i < size){
            if(activityStack!![i] != null){
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    fun appExit(context: Context){
        try {
            finishAllActivity()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityManager.killBackgroundProcesses(context.packageName)
            System.exit(0)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}
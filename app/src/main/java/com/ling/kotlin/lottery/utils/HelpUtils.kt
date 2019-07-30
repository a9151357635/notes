package com.ling.kotlin.lottery.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Handler
import android.os.Message
import android.os.Vibrator
import android.util.SparseIntArray
import com.ling.kotlin.R
import com.ling.kotlin.utils.CacheUtils
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.abs

class SoundPoolUtils(context:Context) {
    private val soundPool:SoundPool by lazy { getSoundPool() }
    private val array = SparseIntArray()
    init {
        array.put(1,soundPool.load(context, R.raw.bet_select, 1))
        array.put(2,soundPool.load(context, R.raw.bet_caveat, 1))
        array.put(3,soundPool.load(context, R.raw.bet_open, 1))
        array.put(4,soundPool.load(context, R.raw.weichat_audio, 1))
    }

    companion object {
        private fun getSoundPool(): SoundPool {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

                SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build()
            } else SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        }
    }

    fun playBetSelect() {
        play(1)
    }

    fun playBetCaveat() {
        play(2)
    }

    fun playBetOpen() {
        play(3)
    }

    fun playInduction() {
        play(4)
    }

    private fun play(key:Int){
        if(CacheUtils.isOpenSound()){
            soundPool?.play(array.get(key), 1f, 1f, 0, 0, 1f)
        }
    }
    fun stop() {
        soundPool.stop(array.get(1))
        soundPool.stop(array.get(2))
        soundPool.stop(array.get(3))
        soundPool.stop(array.get(4))
    }
}

class InductionUtils(context: Context?, private val allItemSize: Int){
    private var sensorManager: SensorManager? = null
    private var defaultSensor: Sensor? = null
    private val shakeListener = ShakeListener()
    private var mHandler: MyHandler? = null
    private var mVibrator: Vibrator? = null//手机震动
    private var mSoundPool: SoundPoolUtils?= null
    //记录摇动状态
    private var isShake = false
    private var mWeiChatAudio: Int = 0

    private val random = Random()
    companion object{
        private const val START_SHAKE = 0x1
        private const val AGAIN_SHAKE = 0x2
        private const val END_SHAKE = 0x3
    }
    init {
        context?.let {
            mSoundPool = SoundPoolUtils(it)
        }
        //获取Vibrator震动服务
        mVibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        defaultSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(shakeListener, defaultSensor, SensorManager.SENSOR_DELAY_UI)
        mHandler = MyHandler(context, object : OnShareListener {
            override fun onShare(index: Int) {
                when (index) {
                    START_SHAKE -> {
                        mVibrator?.vibrate(300)
                        //发出提示音
                        mSoundPool?.playInduction()
                    }
                    AGAIN_SHAKE -> mVibrator?.vibrate(300)
                    END_SHAKE -> isShake = false//整体效果结束, 将震动设置为false
                }
            }
        })
    }


    fun cancelShake() {
        sensorManager?.unregisterListener(shakeListener)
    }

    private class MyHandler(context: Context, private val onShareListener: OnShareListener?) : Handler() {
        private val mReference: WeakReference<Context> = WeakReference(context)
        private var context: Context? = null

        override fun handleMessage(msg: Message) {
            context = mReference.get()
            if (context == null) {
                return
            }
            super.handleMessage(msg)
            onShareListener?.onShare(msg.what)
        }
    }

    inner class ShakeListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val type = event.sensor.type
            if (type == Sensor.TYPE_ACCELEROMETER) {
                //获取三个方向值
                val values = event.values
                if ((abs(values[0]) > 35 || abs(values[1]) > 35 || abs(values[2]) > 35) && !isShake) {
                    isShake = true
                    // 2016/10/19 实现摇动逻辑, 摇动后进行震动
                    Thread{
                        //开始震动 发出提示音 展示动画效果
                        mHandler?.obtainMessage(START_SHAKE)?.sendToTarget()
                        Thread.sleep(500)
                        //再来一次震动提示
                        mHandler?.obtainMessage(AGAIN_SHAKE)?.sendToTarget()
                        Thread.sleep(500)
                        mHandler?.obtainMessage(END_SHAKE)?.sendToTarget()
                        InductionLiveData.postValue(random.nextInt(allItemSize))
                    }.start()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {

        }
    }

    interface OnShareListener {
        fun onShare(index: Int)
    }

}



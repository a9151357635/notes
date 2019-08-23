package com.ling.kotlin.retroft


import android.content.Context
import android.net.*
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import com.ling.kotlin.utils.ContextUtils

/***
 * 网络监听观察者
 */
class ConnectivityLiveData internal constructor(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    constructor() : this(
        ContextUtils.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            postValue(isInternetOn())
        }

        override fun onLost(network: Network?) {
            postValue(false)
        }

        override fun onLosing(network: Network?, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            println("网络即将断开时调用")
        }

        override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            println("网络功能更改")
        }
        override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
            super.onLinkPropertiesChanged(network, linkProperties)
            println("网络连接属性")
        }
        override fun onUnavailable() {
            super.onUnavailable()
            println("网络缺失")
        }
    }

    override fun onActive() {
        super.onActive()
        postValue(isInternetOn())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun isNetworkIsAvailable(): Boolean {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    fun isInternetOn(): Boolean {
        if (isNetworkIsAvailable()) {
            try {
                val p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.baidu.com")
                val value = p.waitFor()
                return value == 0

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }
}
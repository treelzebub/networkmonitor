package net.treelzebub.netdetect

import android.app.Application
import android.content.Context
import android.net.*
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.treelzebub.netdetect.cell.CellQuality
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

open class ConnectionAwareViewModel(context: Context) : AndroidViewModel(context.applicationContext as Application) {

    enum class Quality {
        Good, Poor, None;

        companion object {
            private val all = enumValues<Quality>()

            fun fromString(str: String): Quality {
                return all.find { it.name == str }!!
            }
        }
    }

    val isNetworkConnected = TimestampStringLiveData()
    val cellQuality = TimestampStringLiveData()
    val signalStrength = TimestampStringLiveData()

    private val cell = CellQuality(getApplication())

    /**
     * Tries to open a socket with Google DNS, reports success, and closes the socket. This operation incurs roughly
     * 600 bytes, and optionally runs as a polling operation if you feed it an interval.
     *
     * a 4-minute user session with this running every 1.5 seconds would total about 3.515 kilobytes transferred over
     * the active network.
     */
    suspend fun trySanity() {
        try {
            val timeoutMs = 1000
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)

            val isOnline: Boolean = {
                val connMgr = ContextCompat.getSystemService(getApplication(), ConnectivityManager::class.java)
                connMgr?.activeNetworkInfo?.isConnected == true
            }()

            viewModelScope.launch { isNetworkConnected.value = "Socket Connected? $isOnline" }
            sock.close()
        } catch (e: IOException) {
            Log.e(TAG, "Socket failure.", e)
            viewModelScope.launch { isNetworkConnected.value = "Socket Connected? false" }
        }
    }

    init {
//        registerCallbacks()
        GlobalScope.launch {
            while (true) {
//                DevNet.call()
                trySanity()
                viewModelScope.launch {
                    signalStrength.value = cell.signalStrength.toString()
                    cellQuality.value = cell.current
                }
                delay(1500)
            }
        }
    }

//    private suspend fun set(quality: Quality) {
//        this.isNetworkConnected.value = quality
//    }
//
//    @TargetApi(24)
//    private fun registerCallbacks() {
//        val cm: ConnectivityManager = ContextCompat.getSystemService(getApplication(), ConnectivityManager::class.java)!!
//        val gson = Gson()
//        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
//            val TAG = "NetworkCallback"
//            override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
//                Log.d(TAG, "onCapabilitiesChanged")
//                val isStable = !(networkCapabilities?.hasCapability(NET_CAPABILITY_FOREGROUND) ?: false)
//                Log.d(TAG, "isStable: $isStable")
//            }
//
//            override fun onLost(network: Network?) {
//                Log.d(TAG, "onLost")
//                Log.d(TAG, gson.toJson(network))
//                GlobalScope.launch { set(Quality.None) }
//            }
//
//            override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
//                Log.d(TAG, "onLinkPropertiesChanged")
//                Log.d(TAG, gson.toJson(network))
//                Log.d(TAG, gson.toJson(linkProperties))
//            }
//
//            override fun onUnavailable() {
//                Log.d(TAG, "onUnavailable")
//                GlobalScope.launch { set(Quality.None) }
//            }
//
//            override fun onLosing(network: Network?, maxMsToLive: Int) {
//                Log.d(TAG, "onLosing")
//                Log.d(TAG, gson.toJson(network))
//                Log.d(TAG, gson.toJson(maxMsToLive))
//            }
//
//            override fun onAvailable(network: Network?) {
//                Log.d(TAG, "onAvailable")
//                Log.d(TAG, gson.toJson(network))
//                GlobalScope.launch { set(Quality.Good) }
//            }
//        })
//    }
}
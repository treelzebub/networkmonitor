package net.treelzebub.netdetect._final

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import net.treelzebub.netdetect.connectivityManager
import net.treelzebub.netdetect.wifiManager

data class ConnectivityInfo(
    val isConnected: Boolean,
    val networkType: NetworkType,
    val isMetered: Boolean,
    val signalStrengthDbm: NetworkSignalDbm,
    val rawNetworkInfo: NetworkInfo
) {

    companion object {
        fun create(context: Context): ConnectivityInfo {
            val manager = context.connectivityManager()
            val rawNetworkInfo = manager.activeNetworkInfo
            val networkType = if (rawNetworkInfo.type == ConnectivityManager.TYPE_WIFI) NetworkType.Wifi else NetworkType.Lte
            return ConnectivityInfo(
                isConnected = rawNetworkInfo.isConnected,
                //TODO
                networkType = networkType,
                isMetered = manager.isActiveNetworkMetered,
                signalStrengthDbm = signalStrength(
                    context,
                    networkType
                ),
                rawNetworkInfo = rawNetworkInfo
            )
        }

        private fun signalStrength(context: Context, networkType: NetworkType): NetworkSignalDbm {
            val signal = if (networkType == NetworkType.Wifi) wifiSignal(
                context
            ) else cellSignal(context)
            return NetworkSignalDbm.getRating(networkType, signal)
        }

        private fun wifiSignal(context: Context): Int {
            val rssi = context.wifiManager().connectionInfo.rssi
            return WifiManager.calculateSignalLevel(rssi, 4)
        }

        private fun cellSignal(context: Context): Int {
            return 1
        }
    }

    val isWifi: Boolean
        get() = networkType == NetworkType.Wifi

    override fun toString(): String {
        return "isConnected: $isConnected\n" +
                "networkType: ${networkType.name}\n" +
                "isMetered: $isMetered\n" +
                "signalStrengthDbm: $signalStrengthDbm"
        }
}


interface NetworkMonitor {

    fun hasNetwork(): Boolean

    fun networkType(): NetworkType

    fun networkSignalDbmInt(): Int

    fun networkSignalDbmRating(): NetworkSignalDbm
}

enum class NetworkType {
    Wifi, Edge, ThreeG, Lte
}
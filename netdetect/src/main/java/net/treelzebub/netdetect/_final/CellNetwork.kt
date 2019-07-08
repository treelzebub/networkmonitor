package net.treelzebub.netdetect._final

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.NeighboringCellInfo
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import net.treelzebub.netdetect.TAG
import kotlin.math.min


class CellNetwork(context: Context) : NetworkMonitor {

    private val connectivityManager =
        ContextCompat.getSystemService(context, ConnectivityManager::class.java)!!
    private val telephonyManager: TelephonyManager =
        ContextCompat.getSystemService(context, TelephonyManager::class.java)!!

    val networkSignalDbm: NetworkSignalDbm
        get() = NetworkSignalDbm.getRating(networkType, networkSignalDbmInt())

    val networkType: Int
        get() = connectivityManager.activeNetworkInfo.type

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun hasNetwork(): Boolean {
        return connectivityManager.activeNetworkInfo.isConnected
    }

    override fun networkType(): NetworkType {
        return when (telephonyManager.networkType) {
            NETWORK_TYPE_EDGE -> NetworkType.Edge
            NETWORK_TYPE_LTE -> NetworkType.Lte
            NETWORK_TYPE_GSM -> NetworkType.ThreeG
            else -> TODO()
        }
    }

    override fun networkSignalDbmInt(): Int {
        return try {
            val signalStrength = telephonyManager.signalStrength!!
            Log.d(TAG, gson.toJson(signalStrength))
            if (signalStrength.isGsm) {
                if (signalStrength.gsmSignalStrength <= 2 || signalStrength.gsmSignalStrength == NeighboringCellInfo.UNKNOWN_RSSI) {
                    // Unknown signal strength, get it another way
                    val bits = signalStrength.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    Integer.parseInt(bits[9])
                } else {
                    signalStrength.gsmSignalStrength
                }
            } else {
                val evdoDbm = signalStrength.evdoDbm
                val cdmaDbm = signalStrength.cdmaDbm

                // Use lowest signal to be conservative
                min(cdmaDbm, evdoDbm)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not process signal strength.", e)
            Int.MIN_VALUE
        }
    }

    override fun networkSignalDbmRating(): NetworkSignalDbm {
        return NetworkSignalDbm.getRating(
            networkType(),
            networkSignalDbmInt()
        )
    }
}
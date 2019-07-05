package net.treelzebub.netdetect.net

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat

enum class ConnectionType(val value: Int) {
    Mobile(ConnectivityManager.TYPE_MOBILE),
    Wifi(ConnectivityManager.TYPE_WIFI),
    None(-1);

    companion object {
        private val all = enumValues<ConnectionType>()

        fun from(int: Int): ConnectionType {
            return if (int in all.map { it.value }) {
                all.first { it.value == int }
            } else None
        }
    }
}

class NetworkInfoManager(private val application: Application) {

    fun hasNetwork(): Boolean {
        return getActiveConnectionType() != ConnectionType.None
    }

    fun getActiveConnectionType(): ConnectionType {
        val info = getActiveNetworkInfo() ?: return ConnectionType.None
        return ConnectionType.from(info.type)
    }

    private fun getActiveNetworkInfo(): NetworkInfo? {
        val cm = ContextCompat.getSystemService(application, ConnectivityManager::class.java)!!
        return cm.activeNetworkInfo
    }
}
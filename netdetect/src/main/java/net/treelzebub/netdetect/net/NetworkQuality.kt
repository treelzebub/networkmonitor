package net.treelzebub.netdetect.net

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import net.treelzebub.netdetect.TAG
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkQuality {

    /**
     * Tries to open a socket with Google DNS, reports success, and closes the socket. This operation incurs roughly
     * 600 bytes, and optionally runs as a polling operation if you feed it an interval.
     *
     * a 4-minute user session with this running every 1.5 seconds would total about 3.515 kilobytes transferred over
     * the active network.
     */
    suspend fun trySanity(context: Context, observer: Observer<String>) {
        try {
            val timeoutMs = 1000
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)

            val isOnline: Boolean = {
                val connMgr = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
                connMgr?.activeNetworkInfo?.isConnected == true
            }()

            observer.onChanged("Socket Connected? $isOnline")
            sock.close()
        } catch (e: IOException) {
            Log.e(TAG, "Socket failure.", e)
            observer.onChanged("Socket Connected? false")
        }
    }
}
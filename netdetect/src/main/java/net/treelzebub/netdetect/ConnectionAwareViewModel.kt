package net.treelzebub.netdetect

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.treelzebub.netdetect.cell.CellQuality
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

    fun poll(delay: Long) {
        GlobalScope.launch {
            while (true) {
                trySanity()
                viewModelScope.launch {
                    signalStrength.value = cell.signalStrength
                    cellQuality.value = cell.current
                }
                delay(delay)
            }
        }
    }

    /**
     * Tries to open a socket with Google DNS, reports success, and closes the socket. This operation incurs roughly
     * 600 bytes. A 4-minute user session with this running every 1.5 seconds would total about 3.515 kilobytes
     * transferred over the active network.
     */
    private suspend fun trySanity() {
        try {
            val socket = Socket()
            val address = InetSocketAddress("8.8.8.8", 53)

            socket.connect(address, 1000)
            viewModelScope.launch { isNetworkConnected.value = "Socket Connected? ${socket.isConnected}" }
            socket.close()
        } catch (e: Exception) {
            viewModelScope.launch { isNetworkConnected.value = "Socket Connected? false" }
        }
    }
}
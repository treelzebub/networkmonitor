package net.treelzebub.netdetect._final.strategy

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.treelzebub.netdetect.connectivityManager
import net.treelzebub.netdetect._final.ConnectivityInfo

/**
 *
 */
@TargetApi(23)
internal class MarshmallowConnectivityStrategy : ConnectivityStrategy {

    private val connectivityInfo = MutableLiveData<ConnectivityInfo>()

    override fun observe(context: Context): MutableLiveData<ConnectivityInfo> {
        val manager = context.connectivityManager()
        val requestArchetype = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
            .build()

        manager.registerNetworkCallback(requestArchetype, createCallback(context))
        return connectivityInfo
    }

    private fun createCallback(context: Context): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                update(context)
            }

            override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
                update(context)
            }

            override fun onUnavailable() {
                update(context)
            }
        }
    }

    private fun update(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            connectivityInfo.value = ConnectivityInfo.create(context)
        }
    }
}
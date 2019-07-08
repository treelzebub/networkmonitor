package net.treelzebub.netdetect._final.strategy

/**
 *
 */
//@TargetApi(Build.VERSION_CODES.N)
//internal class NougatConnectivityStrategy : ConnectivityStrategy {
//
//    private val liveData = MutableLiveData<ConnectivityInfo>()
//
//    override fun observe(context: Context): MutableLiveData<ConnectivityInfo> {
//        val manager = ContextCompat.getSystemService(context, ConnectivityManager::class.java)!!
//
//        manager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
//            override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
//                super.onCapabilitiesChanged(network, networkCapabilities)
//
//            }
//
//            override fun onLost(network: Network?) {
//                super.onLost(network)
//            }
//
//            override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
//                super.onLinkPropertiesChanged(network, linkProperties)
//            }
//
//            override fun onUnavailable() {
//                super.onUnavailable()
//            }
//
//            override fun onLosing(network: Network?, maxMsToLive: Int) {
//                super.onLosing(network, maxMsToLive)
//            }
//
//            override fun onAvailable(network: Network?) {
//                super.onAvailable(network)
//            }
//        })
//        TODO()
//    }
//}

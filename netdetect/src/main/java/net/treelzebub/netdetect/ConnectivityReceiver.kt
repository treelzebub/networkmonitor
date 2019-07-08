//package net.treelzebub.netdetect
//
//import android.annotation.TargetApi
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.net.ConnectivityManager
//import androidx.core.content.ContextCompat
//
//class ConnectivityReceiver : BroadcastReceiver() {
//
//    val status = TimestampStringLiveData()
//
//    @TargetApi(23)
//    override fun onReceive(context: Context?, intent: Intent?) {
//        context ?: return; intent ?: return
//        val conn = context.connectivityManager()
//
//        log("Received Action: ${intent.action}",
//            "isMetered: ${conn.isActiveNetworkMetered}",
//            "isDefaultActive: ${conn.isDefaultNetworkActive}",
//            "Bandwidth kbps: ${conn.getNetworkCapabilities(conn.activeNetwork)?.linkDownstreamBandwidthKbps ?: "No active network."}",
//            "isWifi: ${conn.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI}")
//
//        updateIsNetworkAvailable(context)
//    }
//
//    private fun log(vararg strs: String) {
//        status.value = strs.reduce { acc, it -> "$acc\n> $it" }
//    }
//
//    private fun updateIsNetworkAvailable(context: Context) {
//        val connectivity = context.connectivityManager()
//        log("Network Detailed State: " + connectivity.activeNetworkInfo?.detailedState?.toString())
//    }
//}
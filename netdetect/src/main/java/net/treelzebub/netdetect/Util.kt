package net.treelzebub.netdetect


import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


internal val Any.TAG: String
    get() = this::class.java.simpleName

internal fun Context.connectivityManager() = ContextCompat.getSystemService(this, ConnectivityManager::class.java)!!

internal fun Context.telephonyManager() = ContextCompat.getSystemService(this, TelephonyManager::class.java)!!

internal fun Context.wifiManager() = ContextCompat.getSystemService(this, WifiManager::class.java)!!

internal fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

internal fun <T> LiveData<T>.observe(owner: LifecycleOwner, onChanged: (T) -> Unit) = observe(owner, Observer(onChanged))

internal fun <T1, T2> pairOf(t1: T1, t2: T2) = Pair(t1, t2)
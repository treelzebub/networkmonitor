package net.treelzebub.netdetect


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


internal val Any.TAG: String
    get() = this::class.java.simpleName


internal fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

internal fun <T> LiveData<T>.observe(owner: LifecycleOwner, onChanged: (T) -> Unit) = observe(owner, Observer(onChanged))

internal fun <T1, T2> pairOf(t1: T1, t2: T2) = Pair(t1, t2)
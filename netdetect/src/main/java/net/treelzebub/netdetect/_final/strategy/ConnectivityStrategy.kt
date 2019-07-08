package net.treelzebub.netdetect._final.strategy

import android.content.Context
import androidx.lifecycle.MutableLiveData
import net.treelzebub.netdetect._final.ConnectivityInfo

/**
 *
 */
interface ConnectivityStrategy {

    /**
     *
     */
    fun observe(context: Context): MutableLiveData<ConnectivityInfo>
}
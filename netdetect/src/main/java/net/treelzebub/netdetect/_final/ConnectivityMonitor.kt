package net.treelzebub.netdetect._final

//import net.treelzebub.netdetect._final.strategy.LollipopConnectivityStrategy
//import net.treelzebub.netdetect._final.strategy.NougatConnectivityStrategy
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import net.treelzebub.netdetect._final.strategy.ConnectivityStrategy
import net.treelzebub.netdetect._final.strategy.MarshmallowConnectivityStrategy


/**
 *
 */
class ConnectivityMonitor private constructor() {

    companion object {

        /**
         *
         */
        @JvmOverloads
        fun observe(
            context: Context,
            strategy: ConnectivityStrategy = defaultStrategy(),
            onChanged: (ConnectivityInfo) -> Unit
        ): MutableLiveData<ConnectivityInfo> {
            Preconditions.contextIsLifecycle(context)
            val liveData = strategy.observe(context)
            return liveData.also {
                it.observe(context as LifecycleOwner, Observer(onChanged))
            }
        }

        private fun defaultStrategy(): ConnectivityStrategy {
            return MarshmallowConnectivityStrategy()
//            val sdk = Build.VERSION.SDK_INT
//            return when {
//                sdk > Build.VERSION_CODES.N -> NougatConnectivityStrategy()
//                sdk > Build.VERSION_CODES.M -> MarshmallowConnectivityStrategy()
//                sdk > Build.VERSION_CODES.LOLLIPOP -> LollipopConnectivityStrategy()
//                else -> throw IllegalStateException("ConnectivityMonitor must run on at least Lollipop.")
//            }
        }
    }
}
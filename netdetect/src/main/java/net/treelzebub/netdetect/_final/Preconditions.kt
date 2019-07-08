package net.treelzebub.netdetect._final

import android.content.Context
import androidx.lifecycle.LifecycleOwner

object Preconditions {

    fun contextIsLifecycle(context: Context) = require(context is LifecycleOwner) {
        "The Context must be a LifecycleOwner. " +
        "If the caller is a Service, make sure it's a LifecycleService."
    }
}
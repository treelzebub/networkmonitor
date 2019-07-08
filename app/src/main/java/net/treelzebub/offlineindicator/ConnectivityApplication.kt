package net.treelzebub.offlineindicator

import android.app.Application
import net.treelzebub.netdetect.notif.ConnectivityPersistentNotification

class ConnectivityApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ConnectivityPersistentNotification.createChannel(this)
    }
}
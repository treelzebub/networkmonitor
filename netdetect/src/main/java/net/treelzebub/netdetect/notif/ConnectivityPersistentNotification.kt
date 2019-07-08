package net.treelzebub.netdetect.notif

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import net.treelzebub.netdetect.R
import net.treelzebub.netdetect._final.ConnectivityInfo

class ConnectivityPersistentNotification {

    companion object {
        const val NOTIFICATION_ID = 10713
        private const val CHANNEL_ID = "id__connectivity_monitor_channel"
        private const val CHANNEL_NAME = "Persistent"
        private const val CHANNEL_DESC = "A sticky display of current network conditions."

        private fun mgr(context: Context) = ContextCompat.getSystemService(context, NotificationManager::class.java)

        @TargetApi(26)
        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            mgr(context)?.createNotificationChannel(channel)
        }

        fun createOrUpdate(context: Context, data: ConnectivityInfo) {
            val notif = NotificationCompat.Builder(context, CHANNEL_ID)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.notify_panel_notification_icon_bg)
                .setContentTitle("Connectivity Monitor")
                .setContentText(data.toString())
                .build()
            mgr(context)?.notify(NOTIFICATION_ID, notif)
        }
    }
}
package me.twodee.friendlyneighbor

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import net.gotev.uploadservice.BuildConfig
import net.gotev.uploadservice.UploadServiceConfig

class App : Application() {

    companion object {
        // ID of the notification channel used by upload service. This is needed by Android API 26+
        // but you have to always specify it even if targeting lower versions, because it's handled
        // by AndroidX AppCompat library automatically
        const val CHANNEL_1_ID = "TestChannel"
        const val CHANNEL_2_ID = "OnGoingTransaction"

    }

    // Customize the notification channel as you wish. This is only for a bare minimum example
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel1 = NotificationChannel(
                    CHANNEL_1_ID,
                    "TestApp Channel",
                    NotificationManager.IMPORTANCE_HIGH
            )

            val channel2 = NotificationChannel(
                    CHANNEL_2_ID,
                    "TestApp Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        UploadServiceConfig.initialize(
                context = this,
                defaultNotificationChannel = CHANNEL_1_ID,
                debug = BuildConfig.DEBUG
        )
    }
}
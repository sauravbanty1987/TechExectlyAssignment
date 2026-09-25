package com.example.eventmanagement.ui.common

import android.app.*;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage

class EventMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(m: RemoteMessage) {
        val id = "events";
        val n =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager; if (Build.VERSION.SDK_INT >= 26) n.createNotificationChannel(
            NotificationChannel(id, "Event reminders", NotificationManager.IMPORTANCE_DEFAULT)
        ); n.notify(
            System.currentTimeMillis().toInt(),
            NotificationCompat.Builder(this, id).setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(m.notification?.title ?: "Event reminder")
                .setContentText(m.notification?.body ?: "Upcoming event").setAutoCancel(true)
                .build()
        )
    }
}

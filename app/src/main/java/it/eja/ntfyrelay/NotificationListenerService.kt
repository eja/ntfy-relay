// Copyright (C) 2024 by Ubaldo Porcheddu <ubaldo@eja.it>

package it.eja.ntfyrelay

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListenerService : NotificationListenerService() {

    private val sender = NotificationSender()

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE,"")
        val text = extras.getString(Notification.EXTRA_TEXT,"")
        Log.d("NotificationListenerService", "Title: $title, Text: $text")

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val url = sharedPreferences.getString("URL", "")
        var active: Boolean = sharedPreferences.getBoolean("ACTIVE", false)

        if (active == true && url != null && url.toString() != "") {
            val thread = Thread {
                try {
                    sender.sendNotification(url, title, text)
                } catch (e: Exception) {
                    Log.e("NotificationListenerService", "Error sending notification: ${e.message}")
                }
            }
            thread.start()
        }
    }
}

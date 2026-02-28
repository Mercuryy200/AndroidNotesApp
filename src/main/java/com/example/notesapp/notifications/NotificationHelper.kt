package com.example.notesapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.notesapp.R

object NotificationHelper {

    const val CHANNEL_ID = "task_reminders"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_desc)
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}

package black.old.spacedrepetitionowl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// Most notification work is based on tutorial from:
// https://www.raywenderlich.com/1214490-android-notifications-tutorial-getting-started
object NotificationHelper {
    fun createNotificationChannel(context: Context,
                                  importance: Int,
                                  showBadge: Boolean,
                                  name: String,
                                  description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createSampleDataNotification(context: Context,
                                     title: String,
                                     message: String,
                                     bigText: String,
                                     autoCancel: Boolean) {
        val channelId = "${context.packageName}"
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_add_24dp)
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1237, notificationBuilder.build())
    }

    fun createReminderNotification(context: Context,
                                   reminder_id: Long,
                                   title: String,
                                   message: String,
                                   bigText: String,
                                   autoCancel: Boolean) {
        val channelId = "${context.packageName}"
        val notifBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_baseline_subject_24)
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(reminder_id.toInt(), notifBuilder.build())
    }
}
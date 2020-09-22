package black.old.spacedrepetitionowl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmScheduler {
    fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context.applicationContext, AlarmBroadcastReceiver::class.java).apply {
            putExtra("bodytext", "Hello from alarm")
        }

        return PendingIntent.getBroadcast(
            context,
            1338,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createPendingIntentForReminder(context: Context, subjectTitle: String, reminderId: Long): PendingIntent {
        val intent = Intent(context.applicationContext, AlarmBroadcastReceiver::class.java).apply {
            putExtra("reminder_title", subjectTitle)
        }

        return PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun scheduleAlarm(context: Context, alarmTime: Long, alarmIntent: PendingIntent, manager: AlarmManager) {
        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            alarmIntent
        )
    }

}
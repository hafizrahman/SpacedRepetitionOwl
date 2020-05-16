package black.old.spacedrepetitionowl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject


object AlarmScheduler {
    fun createPendingIntent(context: Context): PendingIntent {
        Log.d("FIFTEEN", "AlarmScheduler.createPendingIntent()")
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
        Log.d("FIFTEEN", "AlarmScheduler.scheduleAlarm()")
        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            alarmIntent
        )

    }

}
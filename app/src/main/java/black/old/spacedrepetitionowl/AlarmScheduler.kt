package black.old.spacedrepetitionowl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import black.old.spacedrepetitionowl.models.Subject

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

    fun createReminderPendingIntent(context: Context,
                                    subject: Subject,
                                    reminder_timestamp: Long,
                                    reminder_id: Long) : PendingIntent {
        val intent = Intent(context.applicationContext, AlarmBroadcastReceiver::class.java).apply {
            action = context.getString(R.string.key_bc_notif_action)
            putExtra(context.getString(R.string.key_notif_title), subject.content)
            putExtra(context.getString(R.string.key_notif_subject_id), reminder_id)
        }

        return PendingIntent.getBroadcast(
            context,
            reminder_id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    fun scheduleAlarm(context: Context,
                      alarmTime: Long,
                      alarmIntent: PendingIntent,
                      manager: AlarmManager) {
        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            alarmIntent
        )
    }

}